package com.project.fondea.exception;

import java.util.UUID;

public class CampaignGoalNotReachedException extends RuntimeException {
    public CampaignGoalNotReachedException(String message) {
        super(message);
    }

    public CampaignGoalNotReachedException(UUID campaignId) {
        super("Campaign with id: " + campaignId + " did not reach its goal");
    }
}
