package com.project.fondea.controller;

import com.project.fondea.dto.auth.RegisterUser;
import com.project.fondea.model.enums.Role;
import com.project.fondea.service.AuthService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

    @PostMapping("/register")
        public ResponseEntity<ApiResponse<Void>> registerAdmin(@Valid @RequestBody RegisterUser registerRequest, HttpServletRequest request) {
        authService.register(registerRequest, Role.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        null,
                        "Registro de admin exitoso",
                       request.getRequestURI()
                )
        );
    }
}
