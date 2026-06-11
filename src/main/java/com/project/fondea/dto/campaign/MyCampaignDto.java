package com.project.fondea.dto.campaign;

import com.project.fondea.model.enums.CampaignStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MyCampaignDto(
        UUID id,
        String title,
        BigDecimal goalAmount,
        BigDecimal totalPledged,
        int pledgeCount,
        LocalDate deadline,
        CampaignStatus status,
        int daysLeft,
        BigDecimal availableToWithdraw,
        String rejectionReason
) {}
