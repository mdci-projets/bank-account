package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getAccount(Long accountId, LocalDateTime baselineDate);
}
