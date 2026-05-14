package com.project.fondea.dto.pledge;

import com.project.fondea.model.Pledge;

import java.math.BigDecimal;

public class PledgeMapper {

    public static PledgeCreatedDto toCreated(Pledge pledge) {
        return new PledgeCreatedDto(
                pledge.getId(),
                pledge.getCampaign().getTitle(),
                pledge.getReward() != null ? pledge.getReward().getTitle() : null,
                pledge.getAmount(),
                pledge.getStatus(),
                pledge.getCreatedAt()
        );
    }

    public static MyPledgeDto toMyPledge(Pledge pledge, BigDecimal campaignTotalPledged) {
        return new MyPledgeDto(
                pledge.getId(),
                pledge.getCampaign().getId(),
                pledge.getCampaign().getTitle(),
                pledge.getCampaign().getCreator().getName(),
                pledge.getReward() != null ? pledge.getReward().getTitle() : null,
                pledge.getAmount(),
                pledge.getStatus(),
                pledge.getCreatedAt(),
                pledge.getCampaign().getGoalAmount(),
                campaignTotalPledged,
                pledge.getCampaign().getDeadline()
        );
    }
}