package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.application.response.StatementLine;
import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.OperationTypeEnum;
import com.yma.bank.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatementDomainServiceTest {

    private StatementDomainService statementDomainService;

    @BeforeEach
    void setUp() {
        statementDomainService = new StatementDomainService();
    }

    @Test
    void shouldReturnStatementOnlyForOperationsAfterGivenDate() {
        List<OperationHistory> operations = Arrays.asList(
                new OperationHistory(1L, 1001L, LocalDateTime.of(2024, 2, 25, 10, 0), new BigDecimal("100.00"), OperationTypeEnum.DEPOSIT), // Avant `fromDate`
                new OperationHistory(2L, 1001L, LocalDateTime.of(2024, 3, 5, 14, 0), new BigDecimal("50.00"), OperationTypeEnum.WITHDRAWAL), // Après `fromDate`
                new OperationHistory(3L, 1001L, LocalDateTime.of(2024, 3, 10, 9, 0), new BigDecimal("200.00"), OperationTypeEnum.DEPOSIT) // Après `fromDate`
        );

        LocalDateTime fromDate = LocalDateTime.of(2024, 3, 1, 0, 0);
        AccountStatementResponse response = statementDomainService.generateStatement(1001L, operations, fromDate);

        assertNotNull(response);
        assertEquals(2, response.getStatementLineList().size());

        List<StatementLine> lines = response.getStatementLineList();
        assertEquals(new BigDecimal("250.00"), lines.get(0).getCurrentBalance()); // Après dernier dépôt
        assertEquals(new BigDecimal("50.00"), lines.get(1).getCurrentBalance());  // Après le retrait
    }

    @Test
    void shouldThrowExceptionIfNoTransactionsAfterGivenDate() {
        List<OperationHistory> operations = Arrays.asList(
                new OperationHistory(1L, 1001L, LocalDateTime.of(2024, 3, 1, 10, 0), new BigDecimal("100.00"), OperationTypeEnum.DEPOSIT)
        );

        LocalDateTime fromDate = LocalDateTime.of(2024, 3, 5, 0, 0);

        Exception exception = assertThrows(DomainException.class, () -> {
            statementDomainService.generateStatement(1001L, operations, fromDate);
        });

        assertEquals("No transaction found after the specified date.", exception.getMessage());
    }
}
