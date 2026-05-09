package com.project.fondea.exception;

import java.util.UUID;

public class FraudReportAlreadyExistsException extends RuntimeException {
    public FraudReportAlreadyExistsException(String message) {
        super(message);
    }

    public FraudReportAlreadyExistsException(UUID reporterId, UUID campaignId) {
        super("User " + reporterId + " already reported campaign " + campaignId);
    }
}
