package com.yma.bank.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    public void depositTest() {
        // Given
        Account account = new Account(1234567L,
                BigDecimal.valueOf(0L),
                null);

        // When
        account.deposit(BigDecimal.valueOf(200L));

        // Then
        Iterable<Operation> expected = Collections.singletonList(new Operation(
                null,
                1234567L,
                account.getOperationList().get(0).getTimestamp(),
                BigDecimal.valueOf(200L),
                OperationTypeEnum.DEPOSIT));
        Assertions.assertIterableEquals(expected, account.getOperationList());
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

    @Test
    public void depositAmountNegativeTest() {
        // Given
        Account account = new Account(1234567L,
                BigDecimal.valueOf(0L),
                null);

        // When
        Exception exception = assertThrows(DomainException.class, () -> account.deposit(BigDecimal.valueOf(-300L)));
        assertEquals("The deposit amount must be positive.", exception.getMessage());
    }

    @Test
    public void withdrawTest() {
        // Given
        Account account = new Account(1234567L,
                BigDecimal.valueOf(300L),
                null);

        // When
        Operation actual = account.withdraw(BigDecimal.valueOf(200L));

        // Then
        Iterable<Operation> expected = Collections.singletonList(new Operation(
                null,
                1234567L,
                account.getOperationList().get(0).getTimestamp(),
                BigDecimal.valueOf(200L),
                OperationTypeEnum.WITHDRAWAL));
        Assertions.assertIterableEquals(expected, account.getOperationList());
        Assertions.assertEquals(BigDecimal.valueOf(200L), actual.getAmount());
        assertEquals(OperationTypeEnum.WITHDRAWAL, actual.getOperationType());
        Assertions.assertEquals(1234567L, actual.getAccountId());
    }

    @Test
    public void withdrawAmountPositiveTest() {
        // Given
        Account account = new Account(1234567L,
                BigDecimal.valueOf(300L),
                null);

        // When
        Operation actual = account.withdraw(BigDecimal.valueOf(200L));

        // Then
        Iterable<Operation> expected = Collections.singletonList(new Operation(
                null,
                1234567L,
                account.getOperationList().get(0).getTimestamp(),
                BigDecimal.valueOf(200),
                OperationTypeEnum.WITHDRAWAL));
        Assertions.assertIterableEquals(expected, account.getOperationList());
        Assertions.assertEquals(BigDecimal.valueOf(200L), actual.getAmount());
        Assertions.assertEquals(OperationTypeEnum.WITHDRAWAL, actual.getOperationType());
        Assertions.assertEquals(1234567L, actual.getAccountId());
    }

    @Test
    public void withdrawWhenAmountGreaterThanBalanceTest() {
        // Given
        List<Operation> operationList = createOperationList();
        Account account = new Account(1234567L,
                BigDecimal.valueOf(100L),
                operationList);
        // When
        Exception exception = assertThrows(DomainException.class, () -> account.withdraw(BigDecimal.valueOf(301L)));
        assertEquals("Insufficient balance: Withdrawal of 301 is not possible, current balance: 300", exception.getMessage());
    }

    @Test
    public void calculateBalanceTest() {
        // Given
        List<Operation> operationList = createOperationList();
        Account account = new Account(1234567L,
                BigDecimal.valueOf(500L),
                operationList);

        // When
        BigDecimal actual = account.calculateBalance();

        //Then
        Assertions.assertEquals(BigDecimal.valueOf(700L), actual);
    }

    @Test
    public void calculateBalanceOperationsToDisplayTest() {
        // Given
        List<Operation> operationList = createOperationList();
        Account account = new Account(1234567L,
                BigDecimal.valueOf(300L),
                operationList);

        // When
        BigDecimal actual = account.calculateBalanceOperationsToDisplay();

        //Then
        Assertions.assertEquals(BigDecimal.valueOf(200L), actual);
    }

    private List<Operation> createOperationList() {
        return Arrays.asList(new Operation(null, 123456L, LocalDateTime.now(), BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT),
                new Operation(null, 123456L, LocalDateTime.now(), BigDecimal.valueOf(500L), OperationTypeEnum.DEPOSIT),
                new Operation(null, 123456L, LocalDateTime.now(), BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL),
                new Operation(null, 123456L, LocalDateTime.now(), BigDecimal.valueOf(400L), OperationTypeEnum.WITHDRAWAL));
    }

}
