package com.yma.bank.domain.services;

import com.yma.bank.domain.Account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getAccount(Long accountId, LocalDateTime baselineDate);
    List<Account> getAllAccounts(LocalDateTime baselineDate);
}
