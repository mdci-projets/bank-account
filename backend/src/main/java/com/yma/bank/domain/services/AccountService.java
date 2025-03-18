package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;

public interface AccountService {
    void sendMoney(NewOperationRequest newOperationRequest);
}
