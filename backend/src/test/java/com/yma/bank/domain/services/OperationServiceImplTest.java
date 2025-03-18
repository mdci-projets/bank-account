package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.OperationTypeEnum;
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
import java.util.Arrays;
import java.util.List;

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

    private NewOperationRequest request;
    private Account account;

    @BeforeEach
    void setUp() {
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
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(OperationTypeEnum.WITHDRAWAL, account.getOperationList().get(0).getOperationType());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

    @Test
    public void getAccountStatementTest() {
        // Given
        LocalDateTime currentDate = LocalDateTime.now().minusDays(10);
        Account account = new Account(78965L,
                BigDecimal.valueOf(400L),
                createOperationList(currentDate));
        when(operationRepository.getAccount(78965L, currentDate)).thenReturn(account);

        // When
        AccountStatementResponse actual = operationService.getAccountStatement(78965L, currentDate);

        // Then
        Assertions.assertEquals(78965L, actual.getAccountId());
        Assertions.assertEquals(BigDecimal.valueOf(600L), actual.getStatementLineList().get(0).getCurrentBalance());
        Assertions.assertEquals(BigDecimal.valueOf(200), actual.getStatementLineList().get(0).getAmount());

        Assertions.assertEquals(BigDecimal.valueOf(0L), actual.getStatementLineList().get(1).getCurrentBalance());
        Assertions.assertEquals(BigDecimal.valueOf(-600L), actual.getStatementLineList().get(1).getAmount());

        Assertions.assertEquals(BigDecimal.valueOf(500L), actual.getStatementLineList().get(2).getCurrentBalance());
        Assertions.assertEquals(BigDecimal.valueOf(500L), actual.getStatementLineList().get(2).getAmount());

        Assertions.assertEquals(BigDecimal.valueOf(400L), actual.getStatementLineList().get(3).getCurrentBalance());
        Assertions.assertEquals(BigDecimal.valueOf(-100L), actual.getStatementLineList().get(3).getAmount());
    }

    private List<Operation> createOperationList(LocalDateTime currentDate) {
        return Arrays.asList(new Operation(1L, 78965L, currentDate.minusDays(8), BigDecimal.valueOf(100L), OperationTypeEnum.WITHDRAWAL),
                new Operation(2L, 78965L, currentDate.minusDays(7), BigDecimal.valueOf(500L), OperationTypeEnum.DEPOSIT),
                new Operation(3L, 78965L, currentDate.minusDays(6), BigDecimal.valueOf(600L), OperationTypeEnum.WITHDRAWAL),
                new Operation(4L, 78965L, currentDate.minusDays(5), BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT));
    }

}
