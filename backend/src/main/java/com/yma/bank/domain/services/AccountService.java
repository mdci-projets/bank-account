package com.yma.bank.domain.services;

import com.yma.bank.application.request.NewOperationRequest;
import com.yma.bank.application.response.AccountDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    void sendMoney(NewOperationRequest newOperationRequest);

    AccountDTO getAccount(Long accountId, LocalDateTime baselineDate);

    List<AccountDTO> getAllAccounts(LocalDateTime baselineDate);
}
