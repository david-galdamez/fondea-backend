package com.project.fondea.exception;

import java.math.BigDecimal;

public class WithdrawalLimitExceededException extends RuntimeException {
    public WithdrawalLimitExceededException(String message) {
        super(message);
    }

    public WithdrawalLimitExceededException(BigDecimal limit) {
        super("Daily withdrawal limit of " + limit + " exceeded for new creators");
    }
}
