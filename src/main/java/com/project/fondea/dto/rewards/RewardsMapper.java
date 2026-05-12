package com.project.fondea.dto.rewards;

import com.project.fondea.model.Reward;

public class RewardsMapper {
    public static RewardSummaryDto toRewardsSummary(Reward reward) {
        return new RewardSummaryDto(
                reward.getId(),
                reward.getTitle(),
                reward.getDescription(),
                reward.getMinAmount(),
                reward.getStock(),
                reward.getEstimatedDelivery()
        );
    }
}
