package com.yma.bank.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

public class AccountTest {

    @Test
    public void depositTest() {
        // Given
        Account account = new Account(1234567L,
                BigDecimal.valueOf(0L),
                null);

        // When
        account.deposit(1234567L, BigDecimal.valueOf(200L));

        // Then
        Iterable<Operation> expected = Collections.singletonList(new Operation(
                null,
                1234567L,
                account.getOperationList().get(0).getTimestamp(),
                BigDecimal.valueOf(200L)));
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
        account.deposit(1234567L, BigDecimal.valueOf(-300L));

        // Then
        Iterable<Operation> expected = Collections.singletonList(new Operation(
                null,
                1234567L,
                account.getOperationList().get(0).getTimestamp(),
                BigDecimal.valueOf(300L)));
        Assertions.assertIterableEquals(expected, account.getOperationList());
        Assertions.assertEquals(BigDecimal.valueOf(300L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }
}
