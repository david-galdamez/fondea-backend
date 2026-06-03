package com.project.fondea.model;

import com.project.fondea.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String bio;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;  // ADMIN, CREATOR, SPONSOR

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = true)
    private String verificationCode;

    @Column(nullable = true)
    private LocalDateTime verificationCodeExpiresAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
