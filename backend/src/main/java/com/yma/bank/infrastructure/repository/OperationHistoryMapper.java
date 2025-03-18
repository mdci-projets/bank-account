package com.yma.bank.infrastructure.repository;

import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.OperationTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class OperationHistoryMapper {

    public OperationHistoryEntity toEntity(OperationHistory operation) {
        return new OperationHistoryEntity(
                operation.getId(),
                operation.getAccountId(),
                operation.getAmount(),
                operation.getOperationType().name(),
                operation.getTimestamp()
        );
    }

    public OperationHistory toDomain(OperationHistoryEntity entity) {
        return new OperationHistory(
                entity.getId(),
                entity.getAccountId(),
                entity.getTimestamp(),
                entity.getAmount(),
                OperationTypeEnum.valueOf(entity.getOperationType())
        );
    }
}
