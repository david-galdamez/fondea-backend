package com.project.fondea.service;

import com.project.fondea.dto.campaignUpdate.CampaignUpdateCreatedDto;
import com.project.fondea.dto.campaignUpdate.CampaignUpdateDto;
import com.project.fondea.dto.campaignUpdate.CampaignUpdateMapper;
import com.project.fondea.dto.campaignUpdate.CreateUpdateRequest;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.CampaignNotActiveException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.CampaignUpdate;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.CampaignUpdateRepository;
import com.project.fondea.repository.PledgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignUpdateService {
    private final CampaignUpdateRepository campaignUpdateRepository;
    private final CampaignRepository campaignRepository;
    private final PledgeRepository pledgeRepository;
    private final EmailService emailService;

    @Transactional
    public CampaignUpdateCreatedDto publish(UUID creatorId, UUID campaignId, CreateUpdateRequest request) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if(!campaign.getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        if(campaign.getStatus() == CampaignStatus.DRAFT || campaign.getStatus() == CampaignStatus.UNDER_REVIEW) {
            throw new CampaignNotActiveException(campaign.getId());
        }

        var update = CampaignUpdate.builder()
                .campaign(campaign)
                .title(request.title())
                .body(request.body())
                .visibility(request.visibility())
                .notificationSent(false)
                .build();

        var saved = campaignUpdateRepository.save(update);

        var sponsors = pledgeRepository.findSponsorsByCampaignIdAndStatus(
                campaignId, PledgeStatus.PENDING);
        sponsors.addAll(pledgeRepository.findSponsorsByCampaignIdAndStatus(
                campaignId, PledgeStatus.CHARGED));

        emailService.sendUpdateToSponsors(sponsors, campaign, saved);

        saved.setNotificationSent(true);
        return CampaignUpdateMapper.toCreated(campaignUpdateRepository.save(saved));
    }

    public void delete(UUID updateId, UUID userId) {
        var update = campaignUpdateRepository.findById(updateId)
                .orElseThrow(() -> new EntityNotFoundException("Actualizacion no encontrada"));

        if(!update.getCampaign().getCreator().getId().equals(userId)) {
            throw new BusinessRuleException("No eres duenio de la campania");
        }

        campaignUpdateRepository.delete(update);
    }

    public List<CampaignUpdateDto> getByCampaign(UUID campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("Campaña no encontrada");
        }

        return campaignUpdateRepository.findByCampaignIdOrderByCreatedAtDesc(campaignId)
                .stream()
                .map(CampaignUpdateMapper::toDto)
                .toList();
    }
}
