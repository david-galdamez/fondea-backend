package com.project.fondea.dto.withdrawal;

import com.project.fondea.model.enums.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WithdrawalDto(
        UUID id,
        UUID campaignId,
        String campaignTitle,
        BigDecimal grossAmount,
        BigDecimal commissionAmount,
        BigDecimal netAmount,
        WithdrawalStatus status,
        LocalDateTime requestedAt,
        LocalDateTime paidAt
) {
}
