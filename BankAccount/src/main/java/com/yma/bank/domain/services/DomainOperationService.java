package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.Operation;
import com.yma.bank.domain.OperationTypeEnum;

import java.time.LocalDateTime;

public class DomainOperationService implements OperationService {

    private final OperationRepositoryExtended operationRepository;

    public DomainOperationService(final OperationRepositoryExtended operationRepository) {
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
            throw new IllegalArgumentException("Missing argument 'newOperationRequest'");
        }
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        Account account = operationRepository.getAccount(
                newOperationRequest.getAccountId(),
                baselineDate);

        Long accountId = account.getAccountId()
                .orElseThrow(() -> new DomainException(String.format("Account with %s number not found", newOperationRequest.getAccountId())));

        if (newOperationRequest.getOperationType().equals(OperationTypeEnum.DEPOSIT)) {
            account.deposit(accountId, newOperationRequest.getAmount());

            Operation operation = new Operation(
                    null,
                    accountId,
                    LocalDateTime.now(),
                    newOperationRequest.getAmount());
            operationRepository.saveOperation(operation);
        }
    }
}
