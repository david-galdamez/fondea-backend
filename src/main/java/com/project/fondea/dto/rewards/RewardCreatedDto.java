package com.project.fondea.dto.rewards;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RewardCreatedDto(
        UUID id,
        String title,
        BigDecimal minAmount,
        Integer stock,
        LocalDate estimatedDelivery
) {
}
