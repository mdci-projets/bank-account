package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountStatementResponse;

import java.time.LocalDateTime;

public interface OperationService {
    void sendMoney(NewOperationRequest newOperationRequest);

    AccountStatementResponse getAccountStatement(Long accountId, LocalDateTime baselineDate);
}
