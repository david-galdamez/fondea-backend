package com.project.fondea.repository;

import com.project.fondea.model.FraudReport;
import com.project.fondea.model.enums.FraudReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FraudReportRepository extends JpaRepository<FraudReport, UUID> {
    List<FraudReport> findByStatus(FraudReportStatus status);

    List<FraudReport> findByCampaignId(UUID campaignId);

    boolean existsByReporterIdAndCampaignId(UUID reporterId, UUID campaignId);

    long countByCampaignIdAndStatus(UUID campaignId, FraudReportStatus status);
}
