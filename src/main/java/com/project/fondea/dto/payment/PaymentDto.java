package com.project.fondea.dto.payment;

import com.project.fondea.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDto(
        UUID id,
        UUID pledgeId,
        UUID campaignId,
        String campaignTitle,
        UUID sponsorId,
        String sponsorName,
        BigDecimal amount,
        PaymentStatus status,
        String gatewayRef,
        LocalDateTime chargedAt
) {
}