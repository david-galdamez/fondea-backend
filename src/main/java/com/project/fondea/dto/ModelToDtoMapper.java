package com.project.fondea.dto;

import com.project.fondea.dto.user.UserDto;
import com.project.fondea.model.User;

public class ModelToDtoMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
