package com.yma.bank.application.cli;

import com.yma.bank.application.response.StatementLine;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatementLineFormatter {

    private static final String FIELD_SEPARATOR = "   |   ";

    public String format(final StatementLine statementLine) {
        String operationType = statementLine.getAmount().signum() >= 0 ? "  Credit  " : "  Debit   ";
        StringBuilder sb = new StringBuilder();
        return sb.append(formatDate(statementLine.getTimestamp())).append(FIELD_SEPARATOR)
                .append(operationType).append(FIELD_SEPARATOR)
                .append(formatMoney(statementLine.getAmount())).append(FIELD_SEPARATOR)
                .append(formatMoney(statementLine.getCurrentBalance())).toString();
    }

    private String formatMoney(final BigDecimal amount) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(amount);
    }

    private String formatDate(final LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}