package com.project.fondea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "creator_profile")
public class CreatorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bankAccount;

    @Column(nullable = false)
    private BigDecimal dailyWithdrawalLimit;

    @Column(nullable = false)
    private BigDecimal totalWithdrawnToday = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean isNewCreator = true;
}
