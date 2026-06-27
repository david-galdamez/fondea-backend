package com.project.fondea.service;

import com.project.fondea.dto.user.UserDto;
import com.project.fondea.dto.user.UserMapper;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.repository.CreatorProfileRepository;
import com.project.fondea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CreatorProfileRepository creatorProfileRepository;

    public List<UserDto> listAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public UserDto getById(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return UserMapper.toUserDto(user);
    }
}
