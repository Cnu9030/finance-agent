package com.finance.agent.controller;

import com.finance.agent.dtos.TelegramMessage;
import com.finance.agent.dtos.TelegramUpdate;
import com.finance.agent.dtos.TelegramUserInfo;
import com.finance.agent.entity.TelegramUser;
import com.finance.agent.service.TelegramBotService;
import com.finance.agent.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {

    @Value("${telegram.bot.webhook-secret}")
    private String webhookSecret;

    private final TelegramUserService telegramUserService;
    private final TelegramBotService telegramBotService;

    public TelegramWebhookController(TelegramUserService telegramUserService,TelegramBotService telegramBotService) {
        this.telegramUserService = telegramUserService;
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveUpdate(
            @RequestHeader(
                    name = "X-Telegram-Bot-Api-Secret-Token",
                    required = false
            )
            String receivedSecret,

            @RequestBody TelegramUpdate update) {

        // 1. Validate Telegram webhook secret
        if (receivedSecret == null ||
                !MessageDigest.isEqual(
                        webhookSecret.getBytes(StandardCharsets.UTF_8),
                        receivedSecret.getBytes(StandardCharsets.UTF_8))) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Get the message
        TelegramMessage message = update.message();

        if (message == null || message.from() == null) {
            return ResponseEntity.ok().build();
        }

        // 3. Get Telegram user information
        TelegramUserInfo telegramUser = message.from();

        // 4. Check whether this is a new or returning user
        boolean returningUser = telegramUserService
                .userExists(telegramUser.id());

        // 5. Register or update the user in Supabase
        TelegramUser savedUser = telegramUserService.registerUser(
                telegramUser.id(),
                telegramUser.username(),
                telegramUser.firstName(),
                telegramUser.lastName(),
                telegramUser.languageCode()
        );

        // 6. Select welcome message
        String text = message.text();

        if (text != null) {
            String command = text.trim().split("\\s+", 2)[0];

            // 7. Select the welcome message
            String reply;
                if (returningUser) {
                    reply = "Welcome back to Finance Agent, "
                            + telegramUser.firstName() + "!";
                } else {
                    reply = "Welcome to Finance Agent, "
                            + telegramUser.firstName()
                            + "! Glad to have you here.";
                }

                // 8. Send the message to Telegram
                telegramBotService.sendMessage(
                        message.chat().id(),
                        reply
                );

                System.out.println("Welcome message sent.");
            }


        // 9. Log registration result (don't log secrets)
        System.out.println("Telegram user saved successfully!");
        System.out.println("Database ID: " + savedUser.getId());
        System.out.println("Telegram user ID: " + savedUser.getTelegramUserId());

        return ResponseEntity.ok().build();
    }
}
