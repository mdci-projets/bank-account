package com.yma.bank.application.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class StatementLine {
    private final LocalDateTime timestamp;
    private final BigDecimal amount;
    private final BigDecimal currentBalance;

    public StatementLine(LocalDateTime timestamp, BigDecimal amount, BigDecimal currentBalance) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.currentBalance = currentBalance;
    }
}
