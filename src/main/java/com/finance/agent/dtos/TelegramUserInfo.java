package com.finance.agent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramUserInfo(
        Long id,
        @JsonProperty("is_bot")
        Boolean isBot,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        String username,

        @JsonProperty("language_code")
        String languageCode

) {
}
