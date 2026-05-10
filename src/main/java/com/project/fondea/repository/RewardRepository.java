package com.project.fondea.repository;

import com.project.fondea.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RewardRepository extends JpaRepository<Reward, UUID> {
    List<Reward> findByCampaignId(UUID campaignId);

    @Query("SELECT r FROM Reward r WHERE r.campaign.id = :campaignId " +
        "AND (r.stock IS NULL OR r.stock > 0)")
    List<Reward> findAvailableRewardsByCampaignId(@Param("campaignId") UUID campaignId);
}
