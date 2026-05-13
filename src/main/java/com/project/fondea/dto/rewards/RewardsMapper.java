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

    public static RewardCreatedDto toCreated(Reward reward) {
        return new RewardCreatedDto(
                reward.getId(),
                reward.getTitle(),
                reward.getMinAmount(),
                reward.getStock(),
                reward.getEstimatedDelivery()
        );
    }

    public static RewardDetailDto toDetail(Reward reward, int stockOriginal, int pledgeCount) {
        return new RewardDetailDto(
                reward.getId(),
                reward.getTitle(),
                reward.getDescription(),
                reward.getMinAmount(),
                stockOriginal,
                reward.getStock(),
                pledgeCount,
                reward.getEstimatedDelivery()
        );
    }
}
