package com.project.fondea.service;

import com.project.fondea.dto.ModelToDtoMapper;
import com.project.fondea.dto.auth.LoginRequest;
import com.project.fondea.dto.auth.LoginResponse;
import com.project.fondea.dto.auth.RegisterUser;
import com.project.fondea.dto.user.UserDto;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.IncorrectPasswordException;
import com.project.fondea.exception.UserAlreadyExistsException;
import com.project.fondea.model.CreatorProfile;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.Role;
import com.project.fondea.repository.CreatorProfileRepository;
import com.project.fondea.repository.UserRepository;
import com.project.fondea.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse register(RegisterUser request, Role role) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isVerified(false)
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

        var userDto = ModelToDtoMapper.toUserDto(user);

        return new LoginResponse(jwtUtil.generateToken(userDto));
    }

    public LoginResponse login(LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.getEmail());
        if(user.isEmpty()) {
            throw new EntityNotFoundException("El correo es invalido o no existe");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPasswordHash())) {
            throw new IncorrectPasswordException("La contraseña es incorrecta");
        }

        var userDto = ModelToDtoMapper.toUserDto(user.get());

        return new LoginResponse(jwtUtil.generateToken(userDto));
    }
}
