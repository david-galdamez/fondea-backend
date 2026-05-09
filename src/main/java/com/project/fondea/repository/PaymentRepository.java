package com.project.fondea.repository;

import com.project.fondea.model.Payment;
import com.project.fondea.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPledgeId(UUID pledgeId);

    boolean existsByIdempotencyKey(String idempotencyKey);

    List<Payment> findByStatus(PaymentStatus status);
}
