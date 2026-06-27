package com.project.fondea.service;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.NotificationType;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.PledgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CampaignSettlementService {

    private final CampaignRepository campaignRepository;
    private final PledgeRepository pledgeRepository;
    private final RewardsService rewardService;
    private final PaymentService paymentService;
    private final DonationCertificateService donationCertificateService;
    private final NotificationService notificationService;

    @Transactional
    public void settleExpiredCampaigns() {
        var expiredCampaigns = campaignRepository.findByStatusAndDeadlineBefore(
                CampaignStatus.ACTIVE,
                LocalDate.now()
        );

        expiredCampaigns.forEach(this::settleCampaign);
    }

    private void settleCampaign(Campaign campaign) {
        var totalPledged = campaignRepository.sumPledgesByCampaignIdAndStatus(
                campaign.getId(),
                PledgeStatus.PENDING
        );

        if (totalPledged.compareTo(campaign.getGoalAmount()) >= 0 || campaign.getIsFlexibleGoal()) {
            settleSuccessfulCampaign(campaign);
        } else {
            settleFailedCampaign(campaign);
        }
    }

    private void settleSuccessfulCampaign(Campaign campaign) {
        var pledges = pledgeRepository.findByCampaignIdAndStatus(
                campaign.getId(),
                PledgeStatus.PENDING
        );

        campaign.setStatus(CampaignStatus.SUCCESSFUL);
        campaignRepository.save(campaign);

        pledges.forEach(pledge -> {
            var paymentDto = paymentService.chargePledge(
                    pledge.getId(),
                    "CHARGE-" + pledge.getId()
            );

            var payment = paymentService.findEntityById(paymentDto.id());
            donationCertificateService.createFromPayment(payment);

            notificationService.create(
                    pledge.getSponsor(),
                    campaign,
                    NotificationType.CAMPAIGN_ENDED,
                    "La campaña '" + campaign.getTitle() + "' finalizó exitosamente y tu donación fue cobrada."
            );
        });
    }

    private void settleFailedCampaign(Campaign campaign) {
        var pledges = pledgeRepository.findByCampaignIdAndStatus(
                campaign.getId(),
                PledgeStatus.PENDING
        );

        campaign.setStatus(CampaignStatus.FAILED);
        campaignRepository.save(campaign);

        pledges.forEach(pledge -> {
            pledge.setStatus(PledgeStatus.EXPIRED);
            pledgeRepository.save(pledge);

            if (pledge.getReward() != null) {
                rewardService.increaseStock(pledge.getReward().getId());
            }
            notificationService.create(
                    pledge.getSponsor(),
                    campaign,
                    NotificationType.CAMPAIGN_ENDED,
                    "La campaña '" + campaign.getTitle() + "' no alcanzó la meta. Tu pledge fue marcado como reembolsado."
            );
        });
    }
}