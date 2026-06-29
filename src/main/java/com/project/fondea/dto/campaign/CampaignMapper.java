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
import java.util.UUID;

public class CampaignMapper {

    private static String categoryName(Campaign campaign) {
        return campaign.getCategory() != null ? campaign.getCategory().getName() : null;
    }

    private static UUID categoryId(Campaign campaign) {
        return campaign.getCategory() != null ? campaign.getCategory().getId() : null;
    }

    private static String locationCountry(Campaign campaign) {
        return campaign.getLocation() != null ? campaign.getLocation().getCountry() : null;
    }

    private static String locationCity(Campaign campaign) {
        return campaign.getLocation() != null ? campaign.getLocation().getCity() : null;
    }

    private static UUID locationId(Campaign campaign) {
        return campaign.getLocation() != null ? campaign.getLocation().getId() : null;
    }
    public static CampaignSummaryDto toSummary(Campaign campaign, BigDecimal totalPledged, int pledgeCount) {

        boolean featured = campaign.getFeaturedScore() != null && campaign.getFeaturedScore() > 0;

        return new CampaignSummaryDto(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getCreator().getName(),
                campaign.getCoverImageUrl(),
                campaign.getGoalAmount(),
                totalPledged,
                pledgeCount,
                campaign.getDeadline(),
                categoryName(campaign),
                campaign.getCity(),
                locationCountry(campaign),
                campaign.getStatus(),
                campaign.getFeaturedScore(),
                featured
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
                campaign.getCoverImageUrl(),
                campaign.getCreator().getName(),
                campaign.getCreator().getId(),
                campaign.getGoalAmount(),
                totalPledged,
                pledgeCount,
                daysLeft,
                campaign.getDeadline(),
                campaign.getCreatedAt(),
                campaign.getIsFlexibleGoal(),
                campaign.getStatus(),
                categoryId(campaign),
                locationId(campaign),
                locationCountry(campaign),
                campaign.getCity(),
                rewards,
                faqs,
                campaign.getRejectionReason()
        );
    }

    public static MyCampaignDto toMyCampaign(Campaign campaign,
                                             BigDecimal totalPledged,
                                             int pledgeCount,
                                             BigDecimal committed) {
        int daysLeft = (int) LocalDate.now().until(campaign.getDeadline(), ChronoUnit.DAYS);

        BigDecimal availableToWithdraw = BigDecimal.ZERO;
        if (campaign.getStatus() == CampaignStatus.SUCCESSFUL) {
            availableToWithdraw = totalPledged.subtract(committed).max(BigDecimal.ZERO);
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
                availableToWithdraw,
                campaign.getRejectionReason()
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
                campaign.getCreator().getId(),
                campaign.getCreator().getName(),
                campaign.getCreator().getEmail(),
                campaign.getGoalAmount(),
                campaign.getIsFlexibleGoal(),
                campaign.getDeadline(),
                categoryName(campaign),
                locationCity(campaign),
                campaign.getCreatedAt(),
                campaign.getRejectionReason()
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
}
