package com.project.fondea.dto.withdrawal;

import com.project.fondea.model.enums.WithdrawalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record WithdrawalCreatedDto(
        UUID id,
        BigDecimal grossAmount,
        BigDecimal comissionAmount,
        BigDecimal netAmount,
        WithdrawalStatus status,
        LocalDateTime requestAt
) {
}
