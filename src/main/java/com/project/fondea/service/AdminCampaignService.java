package com.project.fondea.service;

import com.project.fondea.exception.CampaignAlreadyReviewedException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminCampaignService {

    private final CampaignRepository campaignRepository;

    public List<Campaign> getPendingCampaigns() {
        return campaignRepository.findByStatus(CampaignStatus.UNDER_REVIEW);
    }

    public Campaign approveCampaign(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.ACTIVE);

        return campaignRepository.save(campaign);
    }

    public Campaign rejectCampaign(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.UNDER_REVIEW) {
            throw new CampaignAlreadyReviewedException(campaignId);
        }

        campaign.setStatus(CampaignStatus.FAILED);

        return campaignRepository.save(campaign);
    }
}   