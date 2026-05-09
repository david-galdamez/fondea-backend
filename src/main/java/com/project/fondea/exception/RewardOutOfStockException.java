package com.project.fondea.exception;

import java.util.UUID;

public class RewardOutOfStockException extends RuntimeException {
    public RewardOutOfStockException(String message) {
        super(message);
    }

    public RewardOutOfStockException(UUID rewardId) {
        super("Reward with id: " + rewardId + " is out of stock");
    }
}
