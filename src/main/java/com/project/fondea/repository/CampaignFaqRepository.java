package com.project.fondea.repository;

import com.project.fondea.model.CampaignFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignFaqRepository extends JpaRepository<CampaignFaq, UUID> {
    List<CampaignFaq> findByCampaignId(UUID campaignId);
}
