package com.yma.bank.domain;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Operation extends BaseOperation {
    public Operation(Long id, Long accountId, LocalDateTime timestamp, BigDecimal amount, OperationTypeEnum operationType) {
        super(id, accountId, timestamp, amount, operationType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(super.getId(), operation.getId())
                && Objects.equals(super.getAmount(), operation.getAmount())
                && Objects.equals(super.getTimestamp(), operation.getTimestamp())
                && Objects.equals(super.getOperationType(), operation.getOperationType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), super.getAmount(), super.getTimestamp(), super.getOperationType());
    }
}
