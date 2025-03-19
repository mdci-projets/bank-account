package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private OperationHistoryRepository operationHistoryRepository;

    @Mock
    private AccountRepository accountRepository;

    private OperationMapper operationMapper;

    private NewOperationRequest request;
    private Account account;

    @BeforeEach
    void setUp() {
        OperationMapper operationMapper = new OperationMapper();
        accountService = new AccountServiceImpl(operationRepository, operationHistoryRepository, accountRepository, operationMapper);
        request = new NewOperationRequest(1L, new BigDecimal("100"), OperationTypeEnum.DEPOSIT);
        account = new Account(1L, new BigDecimal("500.00"), new ArrayList<>());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        Exception exception = assertThrows(DomainException.class, () -> accountService.sendMoney(null));
        assertEquals("Invalid request: newOperationRequest is null", exception.getMessage());
    }

    @Test
    void shouldThrowDomainExceptionWhenAccountNotFound() {
        when(accountRepository.getAccount(anyLong(), any())).thenThrow(new DomainException("Account not found"));

        Exception exception = assertThrows(DomainException.class, () -> accountService.sendMoney(request));
        assertEquals("Account not found", exception.getMessage());
    }

    @Test
    void shouldProcessDepositSuccessfully() {
        when(accountRepository.getAccount(anyLong(), any())).thenReturn(Optional.of(account));

        accountService.sendMoney(request);

        verify(accountRepository, times(1)).getAccount(anyLong(), any());
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
        when(accountRepository.getAccount(any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(account));

        // When
        accountService.sendMoney(newOperationRequest);

        // Then
        verify(accountRepository).getAccount(any(Long.class), any(LocalDateTime.class));
        verify(operationRepository).saveOperation(any(Operation.class));
        verify(operationHistoryRepository, times(1)).save(any(OperationHistory.class));
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

    @Test
    public void sendMoneyDepositWhenAccountClientNotExistsTest() {
        // Given
        final NewOperationRequest newOperationRequest = new NewOperationRequest(1234567L, BigDecimal.valueOf(200L), OperationTypeEnum.DEPOSIT);
        when(accountRepository.getAccount(eq(1234567L), any(LocalDateTime.class))).thenThrow(new DomainException("Account with %s number not found"));

        // When
        Exception exception = assertThrows(DomainException.class, () -> accountService.sendMoney(newOperationRequest));
        assertEquals("Account with %s number not found", exception.getMessage());
    }

    @Test
    public void sendMoneyWithdrawTest() {
        // Given
        final NewOperationRequest newOperationRequest = new NewOperationRequest(1234567L, BigDecimal.valueOf(200L), OperationTypeEnum.WITHDRAWAL);
        Account account = new Account(newOperationRequest.getAccountId(),
                BigDecimal.valueOf(300L),
                new ArrayList<>());
        when(accountRepository.getAccount(any(Long.class), any(LocalDateTime.class))).thenReturn(Optional.of(account));

        // When
        accountService.sendMoney(newOperationRequest);

        // Then
        verify(accountRepository).getAccount(any(Long.class), any(LocalDateTime.class));
        verify(operationRepository).saveOperation(any(Operation.class));
        verify(operationHistoryRepository, times(1)).save(any(OperationHistory.class));
        Assertions.assertEquals(BigDecimal.valueOf(200L), account.getOperationList().get(0).getAmount());
        Assertions.assertEquals(OperationTypeEnum.WITHDRAWAL, account.getOperationList().get(0).getOperationType());
        Assertions.assertEquals(1234567L, account.getOperationList().get(0).getAccountId());
    }

    @Test
    void shouldReturnAccountWhenExists() {
        LocalDateTime baseLineDate = LocalDateTime.now();
        Account expectedAccount = new Account(1234567L, new BigDecimal("500.00"), null);
        when(accountRepository.getAccount(1234567L, baseLineDate)).thenReturn(Optional.of(expectedAccount));

        AccountDTO retrievedAccount = accountService.getAccount(1234567L, baseLineDate);

        assertEquals(1234567L, retrievedAccount.getAccountId());
        assertEquals(new BigDecimal("500.00"), retrievedAccount.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        LocalDateTime baseLineDate = LocalDateTime.now();
        when(accountRepository.getAccount(1234567L, baseLineDate)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DomainException.class, () -> accountService.getAccount(1234567L, baseLineDate));
        assertEquals("Account not found with ID: 1234567", exception.getMessage());
    }

}
