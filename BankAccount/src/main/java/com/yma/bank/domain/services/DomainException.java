package com.yma.bank.domain.services;

public class DomainException extends RuntimeException {
    DomainException(final String message) {
        super(message);
    }
}
