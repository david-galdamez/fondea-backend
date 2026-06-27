package com.project.fondea.service;

import com.project.fondea.dto.rewards.*;
import com.project.fondea.exception.*;
import com.project.fondea.model.Reward;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.PledgeRepository;
import com.project.fondea.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardsService {

    private final RewardRepository rewardRepository;
    private final CampaignRepository campaignRepository;
    private final PledgeRepository pledgeRepository;

    public RewardCreatedDto create(UUID campaignId, UUID creatorId, CreateRewardRequest request) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(!campaign.getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        if(campaign.getStatus() == CampaignStatus.SUCCESSFUL || campaign.getStatus() == CampaignStatus.FAILED) {
            throw new CampaignNotActiveException(campaignId);
        }

        var reward = Reward.builder()
                .campaign(campaign)
                .title(request.title())
                .description(request.description())
                .minAmount(request.minAmount())
                .stock(request.stock())
                .estimatedDelivery(request.estimatedDelivery())
                .build();

        return RewardsMapper.toCreated(rewardRepository.save(reward));
    }

    public List<RewardSummaryDto> getAvailableByCampaign(UUID campaignId) {
        if(!campaignRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("Campaña no encontrada");
        }

        return rewardRepository.findAvailableRewardsByCampaignId(campaignId)
                .stream()
                .map(RewardsMapper::toRewardsSummary)
                .toList();
    }

    public List<RewardDetailDto> getDetailsByCampaignId(UUID campaignId, UUID creatorId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(!campaign.getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        return rewardRepository.findByCampaignId(campaignId)
                .stream()
                .map(reward -> {
                    int pledgeCount = pledgeRepository.countByRewardId(reward.getId());
                    int stockOriginal = reward.getStock() == null
                            ? -1
                            : reward.getStock() + pledgeCount;
                    return RewardsMapper.toDetail(reward, stockOriginal, pledgeCount);
                })
                .toList();
    }

    public void decreaseStock(UUID rewardId) {
        var reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new EntityNotFoundException("Recompensa no encontrada"));

        if(reward.getStock() == null) return;

        if(reward.getStock() <= 0) {
            throw new RewardOutOfStockException(rewardId);
        }

        reward.setStock(reward.getStock() - 1);
        rewardRepository.save(reward);
    }

    public void increaseStock(UUID rewardId) {
        var reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new EntityNotFoundException("Recompensa no encontrada"));

        if(reward.getStock() == null) return;

        reward.setStock(reward.getStock() + 1);
        rewardRepository.save(reward);
    }

    public void delete(UUID rewardId, UUID creatorId) {
        var reward = rewardRepository.findById(rewardId)
                .orElseThrow(() -> new EntityNotFoundException("Recompensa no encontrada"));

        if(!reward.getCampaign().getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        int pledgeCount = pledgeRepository.countByRewardId(rewardId);
        if(pledgeCount > 0) {
            throw new BusinessRuleException(
                    "No puedes eliminar una recompensa que ya tiene " + pledgeCount + " patrocinadores"
            );
        }

        rewardRepository.delete(reward);
    }

    public Reward getById(UUID rewardId) {
        return rewardRepository.findById(rewardId)
                .orElseThrow(() -> new EntityNotFoundException("Recompensa no encontrada"));
    }
}
