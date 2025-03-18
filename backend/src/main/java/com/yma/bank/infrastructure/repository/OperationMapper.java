package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.Operation;
import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.OperationTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {
    public OperationHistory toHistory(Operation operation) {
        return new OperationHistory(
                operation.getId(),
                operation.getAccountId(),
                operation.getTimestamp(),
                operation.getAmount(),
                operation.getOperationType()
        );
    }

    public Operation toDomain(OperationEntity entity) {
        return new Operation(
                entity.getId(),
                entity.getAccountId(),
                entity.getTimestamp(),
                entity.getAmount(),
                OperationTypeEnum.valueOf(entity.getOperationType())
        );
    }
}
