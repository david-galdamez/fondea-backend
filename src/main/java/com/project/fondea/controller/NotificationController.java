package com.project.fondea.controller;

import com.project.fondea.dto.notification.NotificationDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.NotificationService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthContext authContext;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(HttpServletRequest request) {
        var userId = authContext.getCurrentUserId();
        var notifications = notificationService.getMyNotifications(userId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        notifications,
                        "Notificaciones obtenidas correctamente",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUnreadNotifications(HttpServletRequest request) {
        var userId = authContext.getCurrentUserId();
        var notifications = notificationService.getUnreadNotifications(userId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        notifications,
                        "Notificaciones no leidas obtenidas correctamente",
                        request.getRequestURI()
                )
        );
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(HttpServletRequest request) {
        var userId = authContext.getCurrentUserId();
        notificationService.markAllAsRead(userId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        null,
                        "Notificaciones marcadas como leidas",
                        request.getRequestURI()
                )
        );
    }
}