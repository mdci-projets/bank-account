package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;

public class DomainOperationService implements OperationService {

    private final OperationRepositoryExtended operationRepository;

    public DomainOperationService(final OperationRepositoryExtended operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public void sendMoney(NewOperationRequest createOperationRequest) {
        // TODO
    }
}
