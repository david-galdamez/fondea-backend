package com.project.fondea.repository;

import com.project.fondea.model.CampaignUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, UUID> {
    List<CampaignUpdate> findByCampaignIdOrderByCreatedAtDesc(UUID campaignId);

    List<CampaignUpdate> findByNotificationSentFalse();
}
