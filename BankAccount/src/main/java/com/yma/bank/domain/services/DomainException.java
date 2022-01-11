package com.yma.bank.domain.services;

public class DomainException extends RuntimeException {
    public DomainException(final String message) {
        super(message);
    }
}
