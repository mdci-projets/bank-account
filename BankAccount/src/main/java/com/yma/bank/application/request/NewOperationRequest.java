package com.yma.bank.application.request;

import com.yma.bank.domain.OperationTypeEnum;

import java.math.BigDecimal;

public class NewOperationRequest {
    private final Long accountId;
    private final BigDecimal amount;
    private final OperationTypeEnum operationType;

    public NewOperationRequest(final Long accountId,
                               final BigDecimal amount,
                               final OperationTypeEnum operationType) {
        this.accountId = accountId;
        this.amount = amount;
        this.operationType = operationType;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OperationTypeEnum getOperationType() {
        return operationType;
    }
}
