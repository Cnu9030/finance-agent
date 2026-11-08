package com.finance.agent.controller;

import com.finance.agent.entity.TelegramUser;
import com.finance.agent.service.TelegramUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramUserController {

    private final TelegramUserService telegramUserService;

    public TelegramUserController(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(){
        TelegramUser user = telegramUserService.registerUser(123456789L,
                "test_user",
                "Test",
                "User",
                "en");
        return new ResponseEntity<>(user.getFirstName() + "added successfully",HttpStatus.OK);
    }
}
