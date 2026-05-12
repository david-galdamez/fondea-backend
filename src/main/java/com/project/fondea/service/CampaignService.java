package com.project.fondea.service;

import com.project.fondea.dto.ModelToDtoMapper;
import com.project.fondea.dto.campaign.CampaignDto;
import com.project.fondea.dto.campaign.RegisterCampaignRequest;
import com.project.fondea.exception.CampaignAlreadyReviewedException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.CategoryRepository;
import com.project.fondea.repository.LocationRepository;
import com.project.fondea.repository.UserRepository;
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

    public CampaignDto create(UUID userId, RegisterCampaignRequest registerRequest) {
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

        campaignRepository.save(campaign);

        return ModelToDtoMapper.toCampaignDto(campaign);
    }

    public List<Campaign> getCampaignsById(UUID userId) {
        return campaignRepository.findByCreatorId(userId);
    }

    public List<Campaign> getCampaigns() {
        return campaignRepository.findByStatus(CampaignStatus.ACTIVE);
    }

    public List<Campaign> getDraftedCampaigns(UUID userId) {
        return campaignRepository.findByCreatorIdAndStatus(userId, CampaignStatus.DRAFT);
    }

    public Campaign sendForReview(UUID campaignId, UUID userId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(!campaign.getCreator().getId().equals(userId)) {
            throw new UnauthorizedActionException("No eres el creador de la campaña");
        }

        if(campaign.getStatus() != CampaignStatus.DRAFT) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.UNDER_REVIEW);

        return campaignRepository.save(campaign);
    }

    public Campaign approve(UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.ACTIVE);

        return campaignRepository.save(campaign);
    }

    public Campaign reject(UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.DRAFT);

        return campaignRepository.save(campaign);
    }

    public List<Campaign> getFeatured() {
        return campaignRepository.findByStatusOrderByFeaturedScoreDesc(CampaignStatus.ACTIVE);
    }

    public List<Campaign> findPendingReviw() {
        return campaignRepository.findByStatus(CampaignStatus.UNDER_REVIEW);
    }
}
