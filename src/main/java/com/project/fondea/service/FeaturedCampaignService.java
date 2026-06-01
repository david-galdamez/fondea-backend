package com.project.fondea.service;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FeaturedCampaignService {

    private final CampaignRepository campaignRepository;

    public void recalculateScore(Campaign campaign) {
        var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(
                campaign.getId(),
                PledgeStatus.PENDING
        );

        var pledgeCount = campaignRepository.countPledgesByCampaignId(campaign.getId());

        double goalProgressScore = calculateGoalProgressScore(
                totalPledged,
                campaign.getGoalAmount()
        );

        double pledgeCountScore = pledgeCount * 5.0;

        double urgencyScore = calculateUrgencyScore(campaign);

        double finalScore = goalProgressScore + pledgeCountScore + urgencyScore;

        campaign.setFeaturedScore(finalScore);
        campaignRepository.save(campaign);
    }

    private double calculateGoalProgressScore(BigDecimal totalPledged, BigDecimal goalAmount) {
        if (goalAmount == null || goalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }

        var progress = totalPledged
                .divide(goalAmount, 4, RoundingMode.HALF_UP)
                .doubleValue();

        return Math.min(progress, 1.0) * 60.0;
    }

    private double calculateUrgencyScore(Campaign campaign) {
        if (campaign.getDeadline() == null) {
            return 0.0;
        }

        long daysLeft = LocalDate.now().until(campaign.getDeadline()).getDays();

        if (daysLeft <= 0) {
            return 20.0;
        }

        if (daysLeft <= 3) {
            return 20.0;
        }

        if (daysLeft <= 7) {
            return 12.0;
        }

        if (daysLeft <= 14) {
            return 6.0;
        }

        return 0.0;
    }
}