package com.yma.bank.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public final class OperationHistory {
    private final Long id;
    private final Long accountId;
    private final BigDecimal amount;
    private final OperationTypeEnum operationType;
    private final LocalDateTime timestamp;

    public OperationHistory(Long id, Long accountId, BigDecimal amount, OperationTypeEnum operationType, LocalDateTime timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.operationType = operationType;
        this.timestamp = timestamp;
    }
}
