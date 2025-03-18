package com.yma.bank.domain;

import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public abstract class BaseOperation {
    private final Long id;
    /**
     * The account that owns this operation.
     */
    private final Long accountId;
    /**
     * The money that was deposited or withdrawn.
     */
    private final BigDecimal amount;

    /**
     * The timestamp of the operation.
     */
    private final LocalDateTime timestamp;

    private final OperationTypeEnum operationType;

    protected BaseOperation(Long id, Long accountId, LocalDateTime timestamp, BigDecimal amount, OperationTypeEnum operationType) {
        this.id = id;
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.operationType = operationType;
    }
}
