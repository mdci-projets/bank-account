package com.yma.bank.application.response;

import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class AccountDTO {
    private final Long accountId;
    private final BigDecimal balance;

    public AccountDTO(Long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
}
