package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.domain.Account;
import com.yma.bank.domain.DomainException;
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

        account.getAccountId()
                .orElseThrow(() -> new DomainException(String.format("Account with %s number not found", newOperationRequest.getAccountId())));

        if (newOperationRequest.getOperationType().equals(OperationTypeEnum.DEPOSIT)) {
            Operation operation = account.deposit(newOperationRequest.getAmount());
            operationRepository.saveOperation(operation);
        }

        if (newOperationRequest.getOperationType().equals(OperationTypeEnum.WITHDRAWAL)) {
            Operation operation = account.withdraw(newOperationRequest.getAmount());
            operationRepository.saveOperation(operation);
        }
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
