package com.project.fondea.service;

import com.project.fondea.dto.payment.PaymentDto;
import com.project.fondea.dto.payment.PaymentMapper;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.DuplicatePaymentException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.model.Payment;
import com.project.fondea.model.enums.PaymentStatus;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.PaymentRepository;
import com.project.fondea.repository.PledgeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PledgeRepository pledgeRepository;

    @Transactional
    public PaymentDto chargePledge(UUID pledgeId, String idempotencyKey) {
        if (paymentRepository.existsByIdempotencyKey(idempotencyKey)) {
            throw new DuplicatePaymentException(idempotencyKey);
        }

        var pledge = pledgeRepository.findById(pledgeId)
                .orElseThrow(() -> new EntityNotFoundException("Pledge no encontrado"));

        if (pledge.getStatus() != PledgeStatus.PENDING) {
            throw new BusinessRuleException("Solo se pueden cobrar pledges pendientes");
        }

        var payment = Payment.builder()
                .pledge(pledge)
                .gatewayRef("SIM-" + UUID.randomUUID())
                .idempotencyKey(idempotencyKey)
                .amount(pledge.getAmount())
                .status(PaymentStatus.SUCCESS)
                .chargedAt(LocalDateTime.now())
                .build();

        pledge.setStatus(PledgeStatus.CHARGED);
        pledgeRepository.save(pledge);

        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentDto refundPayment(UUID pledgeId, String idempotencyKey) {
        if (paymentRepository.existsByIdempotencyKey(idempotencyKey)) {
            throw new DuplicatePaymentException(idempotencyKey);
        }

        var pledge = pledgeRepository.findById(pledgeId)
                .orElseThrow(() -> new EntityNotFoundException("Pledge no encontrado"));

        if (pledge.getStatus() == PledgeStatus.REFUNDED) {
            throw new BusinessRuleException("El pledge ya fue reembolsado");
        }

        var payment = Payment.builder()
                .pledge(pledge)
                .gatewayRef("REF-" + UUID.randomUUID())
                .idempotencyKey(idempotencyKey)
                .amount(pledge.getAmount())
                .status(PaymentStatus.REFUNDED)
                .chargedAt(LocalDateTime.now())
                .build();

        pledge.setStatus(PledgeStatus.REFUNDED);
        pledgeRepository.save(pledge);

        return PaymentMapper.toDto(paymentRepository.save(payment));
    }

    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status)
                .stream()
                .map(PaymentMapper::toDto)
                .toList();
    }
}