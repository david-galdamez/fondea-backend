package com.project.fondea.repository;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findByCreatorId(UUID creatorId);

    List<Campaign> findByStatus(CampaignStatus status);

    List<Campaign> findByCategoryIdAndLocationId(UUID categoryId, UUID locationId);

    List<Campaign> findByCategoryId(UUID categoryId);

    List<Campaign> findByLocationId(UUID locationId);

    List<Campaign> findByStatusOrderByFeaturedScoreDesc(CampaignStatus status);

    List<Campaign> findByStatusAndDeadlineBefore(CampaignStatus status, LocalDate date);

    @Query("SELECT c FROM Campaign c WHERE " +
            "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Campaign> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Pledge p " +
            "WHERE p.campaign.id = :campaignId AND p.status = 'PENDING'")
    BigDecimal sumPendingPledgesByCampaignId(@Param("campaignId") Long campaignId);
}
