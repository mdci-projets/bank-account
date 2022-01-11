package com.yma.bank.domain;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
    /**
     * Account number of the client
     */
    private final Long accountId;

    /**
     * The baseline balance of the account. This was the balance of the account before the first
     * operation in the operation List.
     */
    @Getter
    private final BigDecimal baseLineBalance;

    /**
     * The latest operations list to display on this account.
     */
    @Getter
    private final List<Operation> operationList;

    public Account(
            Long accountId,
            BigDecimal baseLineBalance,
            List<Operation> operationList) {
        this.accountId = accountId;
        this.baseLineBalance = baseLineBalance;
        this.operationList = operationList == null || operationList.isEmpty() ? new ArrayList<>(0) : operationList;
    }

    /**
     * Tries to deposit a certain amount of money to this account.
     * Creates a new operation with a positive value.
     */
    public void deposit(@NonNull Long accountId, @NonNull BigDecimal money) {
        // TODO
    }
}
