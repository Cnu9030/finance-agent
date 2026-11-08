package com.finance.agent.service;

import com.finance.agent.entity.TelegramUser;
import com.finance.agent.repository.TelegramUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
public class TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUserService(TelegramUserRepository telegramUserRepository){
        this.telegramUserRepository = telegramUserRepository;
    }

    public boolean userExists(Long telegramUserId) {
        return telegramUserRepository.existsByTelegramUserId(telegramUserId);
    }

    public TelegramUser registerUser(
            Long telegramUserId,
            String username,
            String firstName,
            String lastName,
            String languageCode) {

        TelegramUser user = telegramUserRepository
                .findByTelegramUserId(telegramUserId)
                .orElseGet(TelegramUser::new);

        Instant now = Instant.now();

        // Set createdAt only for a new user
        if (user.getId() == null) {
            user.setCreatedAt(now);
        }

        user.setTelegramUserId(telegramUserId);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLanguageCode(languageCode);
        user.setLastSeenAt(Instant.now());

        return telegramUserRepository.save(user);
    }
}
