package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.domain.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    void sendMoney(NewOperationRequest newOperationRequest);

    Account getAccount(Long accountId, LocalDateTime baselineDate);

    List<Account> getAllAccounts(LocalDateTime baselineDate);
}
