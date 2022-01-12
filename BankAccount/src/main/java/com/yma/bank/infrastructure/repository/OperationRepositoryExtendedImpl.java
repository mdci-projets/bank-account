package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.services.OperationRepositoryExtended;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class OperationRepositoryExtendedImpl implements OperationRepositoryExtended {

    private OperationRepositoryExtended operationRepositoryExtended;

    @Override
    public void saveOperation(Operation operation) {

    }

    @Override
    public Account getAccount(Long accountId, LocalDateTime baselineDate) {
        return null;
    }
}
