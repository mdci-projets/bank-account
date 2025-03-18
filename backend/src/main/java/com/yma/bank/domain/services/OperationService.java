package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;

public interface OperationService {
    void sendMoney(NewOperationRequest newOperationRequest);
}
