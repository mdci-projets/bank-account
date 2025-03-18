package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import com.yma.bank.infrastructure.repository.OperationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class OperationServiceImpl implements OperationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    private final OperationHistoryRepository operationHistoryRepository;

    private final OperationMapper operationMapper;

    public OperationServiceImpl(OperationRepository operationRepository, OperationHistoryRepository operationHistoryRepository, OperationMapper operationMapper) {
        this.operationRepository = operationRepository;
        this.operationHistoryRepository = operationHistoryRepository;
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
        Account account = operationRepository.getAccount(
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
}
