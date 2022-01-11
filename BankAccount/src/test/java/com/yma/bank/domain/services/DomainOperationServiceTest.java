package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.OperationTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DomainOperationServiceTest {

    private OperationService operationService;

    private OperationRepositoryExtended operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository = mock(OperationRepositoryExtended.class);
        operationService = new DomainOperationService(operationRepository);
    }

    @Test
    public void sendMoneyDepositTest() {
        // Given
        final NewOperationRequest newOperationRequest = new NewOperationRequest(1234567L, BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT);
        Account account = new Account(newOperationRequest.getAccountId(),
                BigDecimal.ZERO,
                new ArrayList<>());
        when(operationRepository.getAccount(any(Long.class), any(LocalDateTime.class))).thenReturn(account);

        // When
        operationService.sendMoney(newOperationRequest);

        // Then
        verify(operationRepository).getAccount(any(Long.class), any(LocalDateTime.class));
        verify(operationRepository).saveOperation(any(Operation.class));
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

    @Test
    public void sendMoneyDepositWhenAccountClientNotExistsTest() {
        // Given
        final NewOperationRequest newOperationRequest = new NewOperationRequest(1234567L, BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT);
        when(operationRepository.getAccount(eq(1234567L), any(LocalDateTime.class))).thenThrow(new DomainException("Account with %s number not found"));

        // When
        Exception exception = assertThrows(DomainException.class, () -> operationService.sendMoney(newOperationRequest));
        assertEquals("Account with %s number not found", exception.getMessage());
    }
}
