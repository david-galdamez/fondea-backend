package com.project.fondea.service;

import com.project.fondea.dto.auth.*;
import com.project.fondea.dto.user.UserMapper;
import com.project.fondea.exception.*;
import com.project.fondea.model.CreatorProfile;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.Role;
import com.project.fondea.repository.CreatorProfileRepository;
import com.project.fondea.repository.UserRepository;
import com.project.fondea.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    private static final int CODE_EXPIRATION_MINUTES = 15;

    public MeDto getMe(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return UserMapper.toMeDto(user);
    }

    public MeDto updateProfile(UpdateRequest request, UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        user.setBio(request.bio());
        user.setCountry(request.country());
        user.setName(request.name());
        user.setCity(request.city());

        userRepository.save(user);

        return UserMapper.toMeDto(user);
    }

    @Transactional
    public LoginResponse register(RegisterUser request, Role role) {
        if(userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        String verificationCode =  generateCode();

        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(role)
                .isVerified(false)
                .verificationCode(verificationCode)
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES))
                .build();

        var saved = userRepository.save(user);

        if(saved.getRole() == Role.CREATOR) {
            CreatorProfile profile = CreatorProfile.builder()
                    .user(saved)
                    .dailyWithdrawalLimit(new BigDecimal("500.00"))
                    .totalWithdrawnToday(BigDecimal.ZERO)
                    .isNewCreator(true)
                    .build();
            creatorProfileRepository.save(profile);
        }

        emailService.sendVerificationEmail(saved, verificationCode);

        var userDto = UserMapper.toUserDto(saved);

        return new LoginResponse(
                jwtUtil.generateToken(userDto),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getIsVerified(),
                user.getCreatedAt(),
                user.getBio(),
                user.getCity(),
                user.getCountry()
                );
    }

    @Transactional
    public void verify(UUID userId, String code) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (user.getIsVerified()) {
            throw new BusinessRuleException("El usuario ya está verificado");
        }

        if (user.getVerificationCode() == null ||
                !user.getVerificationCode().equals(code)) {
            throw new BusinessRuleException("El código de verificación es inválido");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("El código de verificación ha expirado");
        }

        // Verificar y limpiar el código
        user.setIsVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }

    @Transactional
    public void resendVerification(UUID userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (user.getIsVerified()) {
            throw new BusinessRuleException("El usuario ya está verificado");
        }

        String newCode = generateCode();
        user.setVerificationCode(newCode);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
        userRepository.save(user);

        emailService.sendVerificationEmail(user, newCode);
    }

    private String generateCode() {
        // Genera un código de 6 dígitos — 000000 a 999999
        return String.format("%06d", new Random().nextInt(999999));
    }

    public LoginResponse login(LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.email()).
                orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if(!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new IncorrectPasswordException("La contraseña es incorrecta");
        }

        var userDto = UserMapper.toUserDto(user);

        return new LoginResponse(
                jwtUtil.generateToken(userDto),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getIsVerified(),
                user.getCreatedAt(),
                user.getBio(),
                user.getCity(),
                user.getCountry()
                );
    }
}
