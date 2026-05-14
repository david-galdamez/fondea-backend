package com.project.fondea.service;

import com.project.fondea.dto.pledge.CreatePledgeRequest;
import com.project.fondea.dto.pledge.MyPledgeDto;
import com.project.fondea.dto.pledge.PledgeCreatedDto;
import com.project.fondea.dto.pledge.PledgeMapper;
import com.project.fondea.exception.*;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.Pledge;
import com.project.fondea.model.Reward;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.PledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PledgeService {

    private final PledgeRepository pledgeRepository;
    private final CampaignRepository campaignRepository;
    private final RewardsService rewardsService;
    //private final NotificationService notificationService;

    private static final BigDecimal NEAR_GOAL_THRESHOLD = new BigDecimal("0.80");

    public PledgeCreatedDto create(UUID sponsorId, CreatePledgeRequest request) {
        var campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new CampaignNotActiveException(request.campaignId());
        }

        if (pledgeRepository.existsBySponsorIdAndCampaignId(sponsorId, request.campaignId())) {
            throw new PledgeAlreadyExistsException(sponsorId, request.campaignId());
        }

        Reward reward = null;
        if (request.rewardId() != null) {
            reward = rewardsService.getById(request.rewardId());

            if (request.amount().compareTo(reward.getMinAmount()) < 0) {
                throw new BusinessRuleException(
                        "El monto mínimo para esta recompensa es " + reward.getMinAmount()
                );
            }

            rewardsService.decreaseStock(request.rewardId());
        }

        var sponsor = new User();
        sponsor.setId(sponsorId);

        var pledge = Pledge.builder()
                .sponsor(sponsor)
                .campaign(campaign)
                .reward(reward)
                .amount(request.amount())
                .status(PledgeStatus.PENDING)
                .build();

        var saved = pledgeRepository.save(pledge);

        checkAndNotifyNearGoal(campaign);

        return PledgeMapper.toCreated(saved);
    }

    public List<MyPledgeDto> getMyPledges(UUID sponsorId) {
        return pledgeRepository.findBySponsorId(sponsorId)
                .stream()
                .map(pledge -> {
                    var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(
                            pledge.getCampaign().getId(), PledgeStatus.PENDING);
                    return PledgeMapper.toMyPledge(pledge, totalPledged);
                })
                .toList();
    }

    private void checkAndNotifyNearGoal(Campaign campaign) {
        var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(
                campaign.getId(), PledgeStatus.PENDING);

        var threshold = campaign.getGoalAmount().multiply(NEAR_GOAL_THRESHOLD);

        if (totalPledged.compareTo(threshold) >= 0) {
            //notificationService.notifyNearGoal(campaign);
        }
    }
}