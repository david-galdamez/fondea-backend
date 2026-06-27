package com.project.fondea.exception;

import java.util.UUID;

public class CampaignAlreadyReviewedException extends RuntimeException {
    public CampaignAlreadyReviewedException(String message) {
        super(message);
    }

    public CampaignAlreadyReviewedException(UUID campaignId) {
        super("Campaign with id: " + campaignId + " has already been reviewed");
    }
}
