package com.project.fondea.controller;

import com.project.fondea.dto.auth.*;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.model.enums.Role;
import com.project.fondea.service.AuthService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthContext authContext;

    // En AuthController
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeDto>> getMe(
            HttpServletRequest request
    ) {
        var userId = authContext.getCurrentUserId();
        var me = authService.getMe(userId);

        return ResponseEntity.ok(ApiResponse.ok(me, "", request.getRequestURI()));
    }
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

    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<MeDto>> updateProfile(
            @Valid @RequestBody UpdateRequest updateRequest,
            HttpServletRequest request
    ) {
        var userId = authContext.getCurrentUserId();
        var updateResponse = authService.updateProfile(updateRequest, userId);

        return ResponseEntity.ok(ApiResponse.ok(updateResponse, "Perfil actualizado correctamente", request.getRequestURI()));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verify(
            @RequestBody VerifyRequest request,
            HttpServletRequest req) {

        // El userId sale del token — el usuario ya está logueado pero no verificado
        var userId = authContext.getCurrentUserId();
        authService.verify(userId, request.code());

        return ResponseEntity.ok(ApiResponse.ok(null, "Cuenta verificada exitosamente", req.getRequestURI()));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>> resendVerification(HttpServletRequest request) {

        var userId = authContext.getCurrentUserId();
        authService.resendVerification(userId);

        return ResponseEntity.ok(ApiResponse.ok(null, "Código reenviado exitosamente", request.getRequestURI()));
    }
}
