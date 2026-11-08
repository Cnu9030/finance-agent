package com.finance.agent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TelegramMessage(

        @JsonProperty("message_id")
        Long messageId,

        @JsonProperty("from")
        TelegramUserInfo from,

        TelegramChat chat,
        String text
) {
}
