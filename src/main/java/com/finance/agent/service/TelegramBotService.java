package com.finance.agent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class TelegramBotService {

    private final RestClient restClient;
    private final String botToken;

    public TelegramBotService(
            @Value("${telegram.bot.token}") String botToken) {

        this.restClient = RestClient.create();
        this.botToken = botToken;
    }

    public void sendMessage(Long chatId, String text) {

        restClient.post()
                .uri("https://api.telegram.org/bot{token}/sendMessage", botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "chat_id", chatId,
                        "text", text
                ))
                .retrieve()
                .toBodilessEntity();
    }
}
