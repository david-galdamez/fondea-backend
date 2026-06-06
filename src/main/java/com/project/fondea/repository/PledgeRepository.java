package com.project.fondea.repository;

import com.project.fondea.model.Pledge;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.PledgeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PledgeRepository extends JpaRepository<Pledge, UUID> {
    List<Pledge> findBySponsorId(UUID sponsorId);

    Page<Pledge> findByCampaignId(UUID campaignId, Pageable pageable);

    List<Pledge> findByCampaignIdAndStatus(UUID campaignId, PledgeStatus status);

    boolean existsBySponsorIdAndCampaignId(UUID sponsorId, UUID campaignId);

    @Query("SELECT DISTINCT p.sponsor FROM Pledge p " +
            "WHERE p.campaign.id = :campaignId AND p.status = :status")
    List<User> findSponsorsByCampaignIdAndStatus(@Param("campaignId") UUID campaignId, @Param("status") PledgeStatus status);


    @Query("SELECT COUNT(p) FROM Pledge p WHERE p.reward.id = :rewardId")
    int countByRewardId(@Param("rewardId") UUID rewardId);

    @Query("SELECT COUNT(p) FROM Pledge p WHERE p.campaign.id = :campaignId")
    int countByCampaignId(@Param("campaignId") UUID campaignId);
}
