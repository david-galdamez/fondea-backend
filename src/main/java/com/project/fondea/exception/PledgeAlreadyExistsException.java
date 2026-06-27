package com.project.fondea.exception;

import java.util.UUID;

public class PledgeAlreadyExistsException extends RuntimeException {
    public PledgeAlreadyExistsException(String message) {
        super(message);
    }

    public PledgeAlreadyExistsException(UUID sponsorId, UUID campaignId) {
        super("Sponsor " + sponsorId + " already pledged to campaign " + campaignId);
    }
}
