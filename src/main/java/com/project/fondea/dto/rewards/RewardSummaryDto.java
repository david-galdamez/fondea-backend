package com.project.fondea.dto.rewards;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RewardSummaryDto(
        UUID id,
        String title,
        String description,
        BigDecimal minAmount,
        Integer stock,               // null = ilimitado
        LocalDate estimatedDelivery
) {
}
