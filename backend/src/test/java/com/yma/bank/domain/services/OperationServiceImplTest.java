package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.*;
import com.yma.bank.infrastructure.repository.OperationMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationServiceImplTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private OperationHistoryRepository operationHistoryRepository;

    private OperationMapper operationMapper;

    private NewOperationRequest request;
    private Account account;

    @BeforeEach
    void setUp() {
        operationMapper = new OperationMapper();
        operationService = new OperationServiceImpl(operationRepository, operationHistoryRepository, operationMapper);
        request = new NewOperationRequest(1L, new BigDecimal("100"), OperationTypeEnum.DEPOSIT);
        account = new Account(1L, new BigDecimal("500.00"), new ArrayList<>());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        Exception exception = assertThrows(DomainException.class, () -> operationService.sendMoney(null));
        assertEquals("Invalid request: newOperationRequest is null", exception.getMessage());
    }

    @Test
    void shouldThrowDomainExceptionWhenAccountNotFound() {
        when(operationRepository.getAccount(anyLong(), any())).thenThrow(new DomainException("Compte introuvable"));

        Exception exception = assertThrows(DomainException.class, () -> operationService.sendMoney(request));
        assertEquals("Compte introuvable", exception.getMessage());
    }

    @Test
    void shouldProcessDepositSuccessfully() {
        when(operationRepository.getAccount(anyLong(), any())).thenReturn(account);

        operationService.sendMoney(request);

        verify(operationRepository, times(1)).getAccount(anyLong(), any());
        verify(operationRepository, times(1)).saveOperation(any(Operation.class));
        verify(operationHistoryRepository, times(1)).save(any(OperationHistory.class));
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
        verify(operationHistoryRepository, times(1)).save(any(OperationHistory.class));
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

    @Test
    public void sendMoneyWithdrawTest() {
        // Given
        final NewOperationRequest newOperationRequest = new NewOperationRequest(1234567L, BigDecimal.valueOf(200L), OperationTypeEnum.WITHDRAWAL);
        Account account = new Account(newOperationRequest.getAccountId(),
                BigDecimal.valueOf(300L),
                new ArrayList<>());
        when(operationRepository.getAccount(any(Long.class), any(LocalDateTime.class))).thenReturn(account);

        // When
        operationService.sendMoney(newOperationRequest);

        // Then
        verify(operationRepository).getAccount(any(Long.class), any(LocalDateTime.class));
        verify(operationRepository).saveOperation(any(Operation.class));
        verify(operationHistoryRepository, times(1)).save(any(OperationHistory.class));
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(OperationTypeEnum.WITHDRAWAL, account.getOperationList().get(0).getOperationType());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

}
