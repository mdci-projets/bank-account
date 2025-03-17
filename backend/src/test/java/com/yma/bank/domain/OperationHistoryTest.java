package com.yma.bank.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OperationHistoryTest {

    @Test
    void shouldCreateOperationHistorySuccessfully() {
        OperationHistory operationHistory = new OperationHistory(
                1L, 1001L, new BigDecimal("100.00"), OperationTypeEnum.DEPOSIT, LocalDateTime.now()
        );

        assertNotNull(operationHistory);
        assertEquals(1L, operationHistory.getId());
        assertEquals(1001L, operationHistory.getAccountId());
        assertEquals(new BigDecimal("100.00"), operationHistory.getAmount());
        assertEquals(OperationTypeEnum.DEPOSIT, operationHistory.getOperationType());
    }

    @Test
    void shouldNotAllowModificationAfterCreation() {
        OperationHistory operationHistory = new OperationHistory(
                1L, 1001L, new BigDecimal("100.00"), OperationTypeEnum.DEPOSIT, LocalDateTime.now()
        );

        assertEquals(new BigDecimal("100.00"), operationHistory.getAmount());
        assertThrows(NoSuchMethodException.class, () -> {
            OperationHistory.class.getDeclaredMethod("setAmount", BigDecimal.class);
        });
    }
}
