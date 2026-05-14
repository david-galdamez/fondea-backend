package com.project.fondea.repository;

import com.project.fondea.model.CampaignFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignFaqRepository extends JpaRepository<CampaignFaq, UUID> {
    // Público — solo las respondidas
    List<CampaignFaq> findByCampaignIdAndAnswerIsNotNullOrderByAskedAtDesc(UUID campaignId);

    // Creador — todas
    List<CampaignFaq> findByCampaignIdOrderByAskedAtDesc(UUID campaignId);

    // Sin responder (para que el creador vea qué le falta)
    List<CampaignFaq> findByCampaignIdAndAnswerIsNull(UUID campaignId);
}
