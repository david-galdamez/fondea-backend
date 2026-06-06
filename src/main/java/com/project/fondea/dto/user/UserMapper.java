package com.project.fondea.dto.user;

import com.project.fondea.dto.auth.MeDto;
import com.project.fondea.model.User;

public class UserMapper {

    public static MeDto toMeDto(User user) {
        return new MeDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCity(),
                user.getCountry(),
                user.getBio(),
                user.getRole().name(),
                user.getIsVerified(),
                user.getCreatedAt()
        );
    }
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
