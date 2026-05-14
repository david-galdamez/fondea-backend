package com.project.fondea.dto.pledge;

import com.project.fondea.model.enums.PledgeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PledgeCreatedDto(
        UUID id,
        String campaignTitle,
        String rewardTitle,
        BigDecimal amount,
        PledgeStatus status,
        LocalDateTime createdAt
) {}