package com.yma.bank.application.cli;

import com.yma.bank.application.response.AccountStatementResponse;
import com.yma.bank.application.response.StatementLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.List;

@Component
public class ConsoleStatementPrinter implements StatementPrinter {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleStatementPrinter.class);
    private final StatementLineFormatter statementLineFormatter = new StatementLineFormatter();
    private final PrintStream printer = System.out;

    @Override
    public void print(AccountStatementResponse accountStatementResponse) {
        if (accountStatementResponse.getStatementLineList().isEmpty()) {
            return;
        }
        LOG.info("<<Account statement for client {}", accountStatementResponse.getAccountId());

        printHeader(printer);

        printStatementLines(accountStatementResponse.getStatementLineList());
    }

    private void printStatementLines(List<StatementLine> accountStatementResponse) {
        for (StatementLine statementLine : accountStatementResponse) {
            printStatementLine(statementLine, printer);
        }
    }

    private void printStatementLine(StatementLine statementLine, PrintStream printer) {
        printer.println(statementLineFormatter.format(statementLine));
    }

    private void printHeader(PrintStream printer) {
        printer.println("DATE                      |    Operation   |   AMOUNT    |   BALANCE");
    }
}
