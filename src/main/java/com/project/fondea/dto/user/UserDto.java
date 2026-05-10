package com.project.fondea.dto.user;

import com.project.fondea.model.enums.Role;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private Role role;
    private boolean isVerified;
}
