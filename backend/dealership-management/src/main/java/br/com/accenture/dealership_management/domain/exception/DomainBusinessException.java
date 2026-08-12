package br.com.accenture.dealership_management.domain.exception;

public final class DomainBusinessException extends RuntimeException {

    public DomainBusinessException(final String message) {
        super(message);
    }
}