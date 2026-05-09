package com.project.fondea.exception;

import java.util.UUID;

public class CampaignNotActiveException extends RuntimeException {
    public CampaignNotActiveException(String message) {
        super(message);
    }

    public CampaignNotActiveException(UUID campaignId) {
        super("Campaign with id: " + campaignId + " is not active");
    }
}
