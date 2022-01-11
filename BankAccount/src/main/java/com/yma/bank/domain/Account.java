package com.yma.bank.domain;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Long> getAccountId() {
        return Optional.of(accountId);
    }

    /**
     * Tries to deposit a certain amount of money to this account.
     * Creates a new operation with a positive value.
     */
    public Operation deposit(@NonNull BigDecimal money) {
        Operation deposit = new Operation(
                null,
                this.accountId,
                LocalDateTime.now(),
                money.signum() < 0 ? money.negate() : money);
        return this.addOperation(deposit);
    }

    /**
     * Tries to retrieve a certain amount of money from this account.
     * Creates a new operation with a negative value.
     * if the given amount is greater than the balance of this account, a DomainException exception is thrown
     */
    public Operation withdraw(@NonNull BigDecimal money) {
        BigDecimal balance = this.calculateBalance();
        if (!mayWithdraw(money, balance)) {
            throw new DomainException(String.format("Maximum threshold for withdrawing money exceeded:: you want to retrieve %s but your balance is %s!", money, balance));
        }

        Operation withdrawal = new Operation(
                null,
                accountId,
                LocalDateTime.now(),
                money.signum() < 0 ? money : money.negate());
        return this.addOperation(withdrawal);
    }

    private boolean mayWithdraw(BigDecimal money, BigDecimal balance) {
        return balance.add(money.negate())
                .compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Add the given operation to operations to display
     *
     * @param operation
     * @return
     */
    public Operation addOperation(Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Missing argument 'operation'");
        }

        this.operationList.add(operation);
        return operation;
    }

    /**
     * Calculates the total balance of the account by adding the operation values to the baseline balance.
     */
    public BigDecimal calculateBalance() {
        return this.baseLineBalance.add(this.calculateBalanceOperationsToDisplay());
    }

    /**
     * Calculates the balance by summing up the values of all operations displayed.
     */
    public BigDecimal calculateBalanceOperationsToDisplay() {
        BigDecimal depositBalance = operationList.stream()
                .map(Operation::getAmount)
                .filter(amount -> amount.signum() >= 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal withdrawalBalance = operationList.stream()
                .map(Operation::getAmount)
                .filter(amount -> amount.signum() < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return depositBalance.add(withdrawalBalance);
    }
}
