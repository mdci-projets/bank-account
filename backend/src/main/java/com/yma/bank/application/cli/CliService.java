package com.yma.bank.application.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Scanner;

@Service
public class CliService {
    private static final Logger LOG = LoggerFactory.getLogger(CliService.class);
    private final Scanner scanner;

    public CliService() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            LOG.warn("Invalid input detected. Expected a number.");
            scanner.next(); // Skip invalid input
        }
        return scanner.nextInt();
    }

    public Long readLong(String message) {
        System.out.print(message);
        while (!scanner.hasNextLong()) {
            LOG.warn("Invalid input detected. Expected a valid account ID.");
            scanner.next(); // Skip invalid input
        }
        return scanner.nextLong();
    }

    public BigDecimal readBigDecimal(String message) {
        System.out.print(message);
        while (!scanner.hasNextBigDecimal()) {
            LOG.warn("Invalid input detected. Expected a valid amount.");
            scanner.next(); // Skip invalid input
        }
        return scanner.nextBigDecimal();
    }
}
