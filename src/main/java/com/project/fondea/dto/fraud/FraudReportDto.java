package com.project.fondea.dto.fraud;

import com.project.fondea.model.enums.FraudReportStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FraudReportDto(
        UUID id,
        UUID reporterId,
        String reporterName,
        String reporterEmail,
        UUID campaignId,
        String campaignTitle,
        String reason,
        FraudReportStatus status,
        String resolutionNotes,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt
) {
}
