package com.project.fondea.dto.rewards;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RewardDetailDto(
    UUID id,
    String title,
    String description,
    BigDecimal minAmount,
    Integer stockOriginal,
    Integer stockRemaining,
    int pledgeCount,
    LocalDate estimatedDelivery
){}
