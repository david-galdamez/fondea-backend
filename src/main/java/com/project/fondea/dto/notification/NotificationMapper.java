package com.project.fondea.dto.notification;

import com.project.fondea.model.Notification;

public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getCampaign() != null ? notification.getCampaign().getId() : null,
                notification.getCampaign() != null ? notification.getCampaign().getTitle() : null,
                notification.getType(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}