package com.project.fondea.dto.withdrawal;

import java.math.BigDecimal;

public record WithdrawalLimitsDto(
        boolean isNewCreator,
        BigDecimal dailyLimit,
        BigDecimal usedToday,
        BigDecimal availableToday
) {}