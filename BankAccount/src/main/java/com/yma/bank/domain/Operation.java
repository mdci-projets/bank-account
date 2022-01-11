package com.yma.bank.domain;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Operation {
    @Getter
    private final Long id;

    /**
     * The account that owns this operation.
     */
    @Getter
    private final Long accountId;

    /**
     * The timestamp of the operation.
     */
    @Getter
    private final LocalDateTime timestamp;

    /**
     * The money that was deposited or withdrawn.
     */
    @Getter
    private final BigDecimal amount;

    public Operation(
            Long id,
            @NonNull Long accountId,
            @NonNull LocalDateTime timestamp,
            @NonNull BigDecimal amount) {
        this.id = id;
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.amount = amount;
    }
}
