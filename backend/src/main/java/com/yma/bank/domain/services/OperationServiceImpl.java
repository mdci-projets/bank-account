package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
import com.yma.bank.domain.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class OperationServiceImpl implements OperationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    public OperationServiceImpl(final OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
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

        switch (newOperationRequest.getOperationType()) {
            case DEPOSIT -> {
                Operation operation = account.deposit(newOperationRequest.getAmount());
                operationRepository.saveOperation(operation);
            }
            case WITHDRAWAL -> {
                Operation operation = account.withdraw(newOperationRequest.getAmount());
                operationRepository.saveOperation(operation);
            }
            default -> throw new DomainException("Invalid operation type : " + newOperationRequest.getOperationType());
        }

        LOGGER.info("Operation successfully recorded for account ID {}", newOperationRequest.getAccountId());
    }

    /**
     * Get account statement since the given date for a given account id
     *
     * @param accountId
     * @param baselineDate
     * @return
     */
    @Override
    public AccountStatementResponse getAccountStatement(Long accountId, LocalDateTime baselineDate) {
        Account account = operationRepository.getAccount(accountId, baselineDate);
        return Utils.generateAccountStatement(account);
    }
}
