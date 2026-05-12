package com.project.fondea.service;

import com.project.fondea.dto.campaign.*;
import com.project.fondea.dto.rewards.RewardsMapper;
import com.project.fondea.exception.CampaignAlreadyReviewedException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RewardRepository rewardRepository;
    private final CampaignFaqRepository faqRepository;

    public CampaignCreatedDto create(UUID userId, RegisterCampaignRequest registerRequest) {
        var creator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        var category = categoryRepository.findById(registerRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));

        var location = locationRepository.findById(registerRequest.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Ubicación no encontrada"));

        var campaign = Campaign.builder()
                .title(registerRequest.getTitle())
                .description(registerRequest.getDescription())
                .goalAmount(registerRequest.getGoalAmount())
                .isFlexibleGoal(registerRequest.getIsFlexibleGoal())
                .deadline(registerRequest.getDeadline())
                .status(CampaignStatus.DRAFT)
                .creator(creator)
                .category(category)
                .location(location)
                .build();

        return CampaignMapper.toCreated(campaignRepository.save(campaign));
    }

    public CampaignDetailDto getCampaignDetails(UUID campaignId) {
        var campaign = campaignRepository.findByIdAndStatus(campaignId, CampaignStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(campaignId, PledgeStatus.PENDING);
        var pledgeCount = campaignRepository.countPledgesByCampaignId(campaignId);

        var rewards = rewardRepository.findAvailableRewardsByCampaignId(campaignId)
                .stream()
                .map(RewardsMapper::toRewardsSummary)
                .toList();

        var faqs = faqRepository.findByCampaignId(campaignId)
                .stream()
                .map(CampaignMapper::toFaq)
                .toList();

        return CampaignMapper.toDetail(campaign, totalPledged, pledgeCount, rewards, faqs);
    }

    public List<MyCampaignDto> getCampaignsByUserId(UUID userId) {
        return campaignRepository.findByCreatorId(userId)
                .stream()
                .map(campaign -> {
                    var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(campaign.getId(), PledgeStatus.PENDING);
                    var pledgeCount = campaignRepository.countPledgesByCampaignId(campaign.getId());
                    return CampaignMapper.toMyCampaign(campaign, totalPledged, pledgeCount);
                })
                .toList();
    }

    public List<CampaignDraftDto> getDraftedCampaigns(UUID userId) {
        return campaignRepository.findByCreatorIdAndStatus(userId, CampaignStatus.DRAFT)
                .stream()
                .map(CampaignMapper::toDraft)
                .toList();
    }

    public List<CampaignSummaryDto> getFeatured() {
        return campaignRepository.findByStatusOrderByFeaturedScoreDesc(CampaignStatus.ACTIVE)
                .stream()
                .map(campaign -> {
                    var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(campaign.getId(), PledgeStatus.PENDING);
                    var pledgeCount = campaignRepository.countPledgesByCampaignId(campaign.getId());
                    return CampaignMapper.toSummary(campaign, totalPledged, pledgeCount);
                })
                .toList();
    }

    public List<CampaignSummaryDto> search(UUID categoryId, UUID locationId, String keyword) {

        List<Campaign> campaigns;

        if (keyword != null && !keyword.isBlank()) {
            campaigns = campaignRepository.searchByKeyword(keyword);
        } else if (categoryId != null && locationId != null) {
            campaigns = campaignRepository.findByCategoryIdAndLocationId(categoryId, locationId);
        } else if (categoryId != null) {
            campaigns = campaignRepository.findByCategoryId(categoryId);
        } else if (locationId != null) {
            campaigns = campaignRepository.findByLocationId(locationId);
        } else {
            campaigns = campaignRepository.findByStatus(CampaignStatus.ACTIVE);
        }

        return campaigns.stream()
                .map(campaign -> {
                    var totalPledged = campaignRepository.sumPendingPledgesByCampaignId(
                            campaign.getId(), PledgeStatus.PENDING);
                    var pledgeCount = campaignRepository.countPledgesByCampaignId(campaign.getId());
                    return CampaignMapper.toSummary(campaign, totalPledged, pledgeCount);
                })
                .toList();
    }

    public List<CampaignReviewDto> findPendingReview() {
        return campaignRepository.findByStatus(CampaignStatus.UNDER_REVIEW)
                .stream()
                .map(CampaignMapper::toReview)
                .toList();
    }

    public void sendForReview(UUID campaignId, UUID userId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (!campaign.getCreator().getId().equals(userId)) {
            throw new UnauthorizedActionException("No eres el creador de la campaña");
        }

        if (campaign.getStatus() != CampaignStatus.DRAFT) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.UNDER_REVIEW);
        campaignRepository.save(campaign);
    }

    public CampaignStatusDto approve(UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.ACTIVE);
        return CampaignMapper.toStatus(campaignRepository.save(campaign));
    }

    public CampaignStatusDto reject(UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.DRAFT);
        return CampaignMapper.toStatus(campaignRepository.save(campaign));
    }
}