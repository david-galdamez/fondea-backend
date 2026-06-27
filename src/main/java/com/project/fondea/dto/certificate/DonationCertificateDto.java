package com.project.fondea.dto.certificate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DonationCertificateDto(
        UUID id,
        UUID pledgeId,
        UUID campaignId,
        String campaignTitle,
        String sponsorName,
        String sponsorEmail,
        String fiscalName,
        String fiscalId,
        BigDecimal amount,
        LocalDateTime issuedAt,
        String pdfUrl
) {
}