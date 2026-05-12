package com.project.fondea.dto.campaign;

import com.project.fondea.dto.faq.FaqDto;
import com.project.fondea.dto.rewards.RewardSummaryDto;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.CampaignFaq;
import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CampaignMapper {
    public static CampaignSummaryDto toSummary(Campaign campaign, BigDecimal totalPledged, int pledgeCount) {
        return new CampaignSummaryDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getCreator().getName(),
                campaign.getGoalAmount(),
                totalPledged,
                pledgeCount,
                campaign.getDeadline(),
                campaign.getCategory().getName(),
                campaign.getLocation().getCity(),
                campaign.getStatus(),
                campaign.getFeaturedScore()
        );
    }

    public static CampaignDetailDto toDetail(Campaign campaign,
                                             BigDecimal totalPledged,
                                             int pledgeCount,
                                             List<RewardSummaryDto> rewards,
                                             List<FaqDto> faqs) {
        int daysLeft = (int) LocalDate.now().until(campaign.getDeadline(), ChronoUnit.DAYS);

        return new CampaignDetailDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getCreator().getName(),
                campaign.getCreator().getId(),
                campaign.getGoalAmount(),
                totalPledged,
                pledgeCount,
                daysLeft,
                campaign.getDeadline(),
                campaign.getIsFlexibleGoal(),
                campaign.getStatus(),
                campaign.getCategory().getName(),
                campaign.getLocation().getCity(),
                campaign.getLocation().getCountry(),
                rewards,
                faqs
        );
    }

    public static MyCampaignDto toMyCampaign(Campaign campaign,
                                             BigDecimal totalPledged,
                                             int pledgeCount) {
        int daysLeft = (int) LocalDate.now().until(campaign.getDeadline(), ChronoUnit.DAYS);

        BigDecimal availableToWithdraw = BigDecimal.ZERO;
        if (campaign.getStatus() == CampaignStatus.SUCCESSFUL) {
            availableToWithdraw = totalPledged.multiply(new BigDecimal("0.95"));
        }

        return new MyCampaignDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getGoalAmount(),
                totalPledged,
                pledgeCount,
                campaign.getDeadline(),
                campaign.getStatus(),
                daysLeft,
                availableToWithdraw
        );
    }

    public static CampaignDraftDto toDraft(Campaign campaign) {
        return new CampaignDraftDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getGoalAmount(),
                campaign.getDeadline(),
                campaign.getCreatedAt()
        );
    }

    public static CampaignReviewDto toReview(Campaign campaign) {
        return new CampaignReviewDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getDescription(),
                campaign.getCreator().getName(),
                campaign.getCreator().getEmail(),
                campaign.getGoalAmount(),
                campaign.getIsFlexibleGoal(),
                campaign.getDeadline(),
                campaign.getCategory().getName(),
                campaign.getLocation().getCity(),
                campaign.getCreatedAt()
        );
    }

    public static CampaignStatusDto toStatus(Campaign campaign) {
        return new CampaignStatusDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getStatus()
        );
    }

    public static CampaignCreatedDto toCreated(Campaign campaign) {
        return new CampaignCreatedDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getStatus(),
                campaign.getCreatedAt()
        );
    }

    public static FaqDto toFaq(CampaignFaq faq) {
        return new FaqDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer()
        );
    }
}
