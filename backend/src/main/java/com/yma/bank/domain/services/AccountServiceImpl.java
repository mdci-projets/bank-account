package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import com.yma.bank.infrastructure.repository.OperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final OperationRepository operationRepository;

    private final OperationHistoryRepository operationHistoryRepository;

    private final AccountRepository accountRepository;

    private final OperationMapper operationMapper;

    public AccountServiceImpl(OperationRepository operationRepository,
                              OperationHistoryRepository operationHistoryRepository,
                              AccountRepository accountRepository,
                              OperationMapper operationMapper
                              ) {
        this.operationRepository = operationRepository;
        this.operationHistoryRepository = operationHistoryRepository;
        this.accountRepository = accountRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * deposit a certain amount of money to the given account.
     * if the given account isn't, a DomainException exception is thrown
     * If successful, a new operation with a positive value is created.
     *
     * @param newOperationRequest
     */
    @Override
    public void sendMoney(NewOperationRequest newOperationRequest) {
        if (newOperationRequest == null) {
            throw new DomainException("Invalid request: newOperationRequest is null");
        }
        LOGGER.info("Processing transaction for account ID: {}", newOperationRequest.getAccountId());

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        Account account = getAccount(
                newOperationRequest.getAccountId(),
                baselineDate);

        account.getAccountId()
                .orElseThrow(() -> new DomainException(String.format("Account with %s number not found", newOperationRequest.getAccountId())));

        Operation operation;
        switch (newOperationRequest.getOperationType()) {
            case DEPOSIT -> operation = account.deposit(newOperationRequest.getAmount());
            case WITHDRAWAL -> operation = account.withdraw(newOperationRequest.getAmount());
            default -> throw new DomainException("Invalid operation type : " + newOperationRequest.getOperationType());
        }

        operationRepository.saveOperation(operation);
        operationHistoryRepository.save(operationMapper.toHistory(operation));

        LOGGER.info("Operation successfully recorded for account ID {}", newOperationRequest.getAccountId());
    }

    @Override
    public Account getAccount(Long accountId, LocalDateTime baselineDate) {
        LOGGER.info("Searching for account ID {}", accountId);
        return accountRepository.getAccount(accountId, baselineDate)
                .orElseThrow(() -> new DomainException(String.format("Account not found with ID: %s", accountId)));
    }


}
