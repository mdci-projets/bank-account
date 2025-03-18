package com.yma.bank.domain.services;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.OperationHistory;
import com.yma.bank.domain.OperationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @InjectMocks
    private StatementServiceImpl statementService;

    @Mock
    private OperationHistoryRepository operationHistoryRepository;

    private StatementDomainService statementDomainService;

    @BeforeEach
    void setup() {
        statementDomainService = new StatementDomainService();
        statementService = new StatementServiceImpl(operationHistoryRepository, statementDomainService);
    }

    @Test
    void shouldGenerateAccountStatementSuccessfully() {
        // Given
        LocalDateTime currentDate = LocalDateTime.now();
        Long accountId = 78965L;

        // Simuler les opérations en base de données
        List<OperationHistory> mockOperations = createOperationList(currentDate);
        when(operationHistoryRepository.findByAccountId(accountId)).thenReturn(mockOperations);

        // When
        AccountStatementResponse actual = statementService.generateAccountStatement(accountId, currentDate.minusDays(10));

        // Then
        assertNotNull(actual);
        assertEquals(accountId, actual.getAccountId());
        assertEquals(4, actual.getStatementLineList().size());

        // Vérification des soldes
        assertEquals(BigDecimal.valueOf(500L), actual.getStatementLineList().get(0).getCurrentBalance()); // Solde après dépôt 500 (il y avait 300)
        assertEquals(BigDecimal.valueOf(300L), actual.getStatementLineList().get(1).getCurrentBalance()); // Ajout de 100
        assertEquals(BigDecimal.valueOf(900L), actual.getStatementLineList().get(2).getCurrentBalance()); // Retrait de 600
        assertEquals(BigDecimal.valueOf(800L), actual.getStatementLineList().get(3).getCurrentBalance()); // Dépôt de 200

        // Vérification des montants
        assertEquals(BigDecimal.valueOf(200L), actual.getStatementLineList().get(0).getAmount()); // Dépôt
        assertEquals(BigDecimal.valueOf(-600L), actual.getStatementLineList().get(1).getAmount()); // Retrait
        assertEquals(BigDecimal.valueOf(100L), actual.getStatementLineList().get(2).getAmount()); // Dépôt
        assertEquals(BigDecimal.valueOf(500L), actual.getStatementLineList().get(3).getAmount()); // Dépôt

        verify(operationHistoryRepository, times(1)).findByAccountId(accountId);
    }

    @Test
    void shouldReturnEmptyStatementWhenNoTransactionsExist() {
        // Given
        LocalDateTime currentDate = LocalDateTime.now().minusDays(10);
        Long accountId = 78965L;

        when(operationHistoryRepository.findByAccountId(accountId)).thenReturn(List.of());

        // When
        Exception exception = assertThrows(DomainException.class, () -> {
            statementService.generateAccountStatement(accountId, currentDate);
        });

        // Then
        assertEquals("No transaction found after the specified date.", exception.getMessage());
        verify(operationHistoryRepository, times(1)).findByAccountId(accountId);
    }

    private List<OperationHistory> createOperationList(LocalDateTime currentDate) {
        return List.of(
                new OperationHistory(1L, 78965L, currentDate.minusDays(8), BigDecimal.valueOf(500L), OperationTypeEnum.DEPOSIT),
                new OperationHistory(2L, 78965L, currentDate.minusDays(7), BigDecimal.valueOf(100L), OperationTypeEnum.DEPOSIT),
                new OperationHistory(3L, 78965L, currentDate.minusDays(6), BigDecimal.valueOf(600L), OperationTypeEnum.WITHDRAWAL),
                new OperationHistory(4L, 78965L, currentDate.minusDays(5), BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT),
                new OperationHistory(5L, 78965L, currentDate.minusDays(12), BigDecimal.valueOf(300L), OperationTypeEnum.DEPOSIT)
        );
    }
}
