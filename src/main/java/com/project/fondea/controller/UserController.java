package com.project.fondea.controller;

import com.project.fondea.dto.user.UserDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.UserService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthContext authContext;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> listAll(HttpServletRequest request) {
        var users = userService.listAll();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        users,
                        "Usuarios obtenidos correctamente",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getById(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var user = userService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        user,
                        "Usuario obtenido correctamente",
                        request.getRequestURI()
                )
        );
    }
}
