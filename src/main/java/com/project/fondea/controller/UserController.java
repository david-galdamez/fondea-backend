package com.project.fondea.controller;

import com.project.fondea.dto.auth.LoginRequest;
import com.project.fondea.dto.auth.LoginResponse;
import com.project.fondea.dto.auth.RegisterUser;
import com.project.fondea.model.enums.Role;
import com.project.fondea.service.AuthService;
import com.project.fondea.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/register-creator")
    public ResponseEntity<ApiResponse<LoginResponse>> registerCreator(@Valid @RequestBody RegisterUser registerRequest) {
        var registerResponse = authService.register(registerRequest, Role.CREATOR);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        registerResponse,
                        "Registro de usuario exitoso"
                )
        );
    }

    @PostMapping("/register-sponsor")
    public ResponseEntity<ApiResponse<LoginResponse>> registerSponsor(@Valid @RequestBody RegisterUser registerRequest) {
        var registerResponse = authService.register(registerRequest, Role.SPONSOR);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        registerResponse,
                        "Registro de usuario exitoso"
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        var loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        loginResponse,
                        "Inicion de sesion exitoso"
                )
        );
    }
}
