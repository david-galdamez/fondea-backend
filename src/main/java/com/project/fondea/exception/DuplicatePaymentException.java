package com.project.fondea.exception;

public class DuplicatePaymentException extends RuntimeException {

    public DuplicatePaymentException(String idempotencyKey) {
        super("Payment already processed with key: " + idempotencyKey);
    }
}
