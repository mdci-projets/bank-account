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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(accountId, operation.accountId)
                && Objects.equals(amount, operation.amount)
                && Objects.equals(timestamp, operation.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, amount, timestamp);
    }
}
