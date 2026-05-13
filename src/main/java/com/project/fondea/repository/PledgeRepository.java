package com.project.fondea.repository;

import com.project.fondea.model.Pledge;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.PledgeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, UUID> {
    int countByRewardId(UUID rewardId);
    List<Pledge> findBySponsorId(UUID sponsorId);

    List<Pledge> findByCampaignId(UUID campaignId);

    List<Pledge> findByCampaignIdAndStatus(UUID campaignId, PledgeStatus status);

    boolean existsBySponsorIdAndCampaignId(UUID sponsorId, UUID campaignId);

    @Query("SELECT DISTINCT p.sponsor FROM Pledge p " +
            "WHERE p.campaign.id = :campaignId AND p.status = 'PENDING'")
    List<User> findSponsorsByCampaignId(@Param("campaignId") UUID campaignId);
}
