package com.yma.bank.domain.services;

import com.yma.bank.domain.Operation;

public interface OperationRepository {
    void saveOperation(Operation operation);
}
