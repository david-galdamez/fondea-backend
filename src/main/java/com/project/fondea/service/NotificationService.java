package com.project.fondea.service;

import com.project.fondea.dto.notification.NotificationDto;
import com.project.fondea.dto.notification.NotificationMapper;
import com.project.fondea.model.Campaign;
import com.project.fondea.model.Notification;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.NotificationType;
import com.project.fondea.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getMyNotifications(UUID userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    public List<NotificationDto> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId)
                .stream()
                .map(NotificationMapper::toDto)
                .toList();
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }

    public void create(User user, Campaign campaign, NotificationType type, String message) {
        var notification = Notification.builder()
                .user(user)
                .campaign(campaign)
                .type(type)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}