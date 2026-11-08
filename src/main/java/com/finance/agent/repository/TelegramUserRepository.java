package com.finance.agent.repository;

import com.finance.agent.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByTelegramUserId(Long telegramUserId );

    boolean existsByTelegramUserId(Long telegramUserId);
}
