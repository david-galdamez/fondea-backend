package com.project.fondea.dto.notification;

import com.project.fondea.model.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        UUID campaignId,
        String campaignTitle,
        NotificationType type,
        String message,
        Boolean isRead,
        LocalDateTime createdAt
) {
}