package com.yma.bank.domain.services;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;

import java.time.LocalDateTime;

public interface OperationRepositoryExtended {
    void saveOperation(Operation operation);

    Account getAccount(Long accountId, LocalDateTime baselineDate);
}
