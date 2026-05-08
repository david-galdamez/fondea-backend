package com.project.fondea.models;

import com.project.fondea.models.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    private BigDecimal grossAmount;       // Lo que había disponible
    private BigDecimal commissionAmount;  // El 5%
    private BigDecimal netAmount;         // Lo que realmente recibe

    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status; // PENDING, APPROVED, REJECTED, PAID

    @CreationTimestamp
    private LocalDateTime requestedAt;
    private LocalDateTime paidAt;
}
