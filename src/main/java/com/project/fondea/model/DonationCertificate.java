package com.project.fondea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donation_certificates")
public class DonationCertificate {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "pledge_id", nullable = false)
    private Pledge pledge;

    @ManyToOne
    @JoinColumn(name = "sponsor_id", nullable = false)
    private User sponsor;

    private String fiscalName;
    private String fiscalId;
    private BigDecimal amount;
    private LocalDateTime issuedAt;
    private String pdfUrl;
}
