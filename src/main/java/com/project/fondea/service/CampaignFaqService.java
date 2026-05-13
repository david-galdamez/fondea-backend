package com.project.fondea.service;

import com.project.fondea.dto.faq.*;
import com.project.fondea.exception.CampaignNotActiveException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.CampaignFaq;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.repository.CampaignFaqRepository;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignFaqService {

    private final CampaignFaqRepository faqRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public FaqDto ask(UUID sponsorId, UUID campaignId, AskQuestionRequest request) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new CampaignNotActiveException(campaignId);
        }

        var sponsor = userRepository.findById(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        var faq = CampaignFaq.builder()
                .campaign(campaign)
                .askedBy(sponsor)
                .question(request.question())
                .answer(null)
                .answeredAt(null)
                .build();

        return FaqMapper.toDto(faqRepository.save(faq));
    }

    public FaqDto answer(UUID creatorId, UUID faqId, AnswerQuestionRequest request) {
        var faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada"));

        if (!faq.getCampaign().getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        if (faq.getAnswer() != null) {
            throw new UnauthorizedActionException("Esta pregunta ya fue respondida");
        }

        faq.setAnswer(request.answer());
        faq.setAnsweredAt(LocalDateTime.now());

        return FaqMapper.toDto(faqRepository.save(faq));
    }

    public List<FaqDto> getPublicFaqs(UUID campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("Campaña no encontrada");
        }

        return faqRepository
                .findByCampaignIdAndAnswerIsNotNullOrderByAskedAtDesc(campaignId)
                .stream()
                .map(FaqMapper::toDto)
                .toList();
    }

    public List<FaqManageDto> getManageFaqs(UUID creatorId, UUID campaignId) {
        var campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (!campaign.getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        return faqRepository.findByCampaignIdOrderByAskedAtDesc(campaignId)
                .stream()
                .map(FaqMapper::toManageDto)
                .toList();
    }

    public void delete(UUID creatorId, UUID faqId) {
        var faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada"));

        if (!faq.getCampaign().getCreator().getId().equals(creatorId)) {
            throw new UnauthorizedActionException("No eres el creador de esta campaña");
        }

        faqRepository.delete(faq);
    }
}