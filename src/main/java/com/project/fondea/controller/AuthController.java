package com.project.fondea.controller;

import com.project.fondea.dto.auth.LoginRequest;
import com.project.fondea.dto.auth.LoginResponse;
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
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-creator")
    public ResponseEntity<ApiResponse<LoginResponse>> registerCreator(@Valid @RequestBody RegisterUser registerRequest, HttpServletRequest request) {
        var registerResponse = authService.register(registerRequest, Role.CREATOR);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        registerResponse,
                        "Registro de usuario exitoso",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/register-sponsor")
    public ResponseEntity<ApiResponse<LoginResponse>> registerSponsor(@Valid @RequestBody RegisterUser registerRequest, HttpServletRequest request) {
        var registerResponse = authService.register(registerRequest, Role.SPONSOR);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        registerResponse,
                        "Registro de usuario exitoso",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        var loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        loginResponse,
                        "Inicion de sesion exitoso",
                        request.getRequestURI()
                )
        );
    }
}
