package com.project.fondea.service;

import com.project.fondea.dto.certificate.DonationCertificateDto;
import com.project.fondea.dto.certificate.DonationCertificateMapper;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.model.DonationCertificate;
import com.project.fondea.model.Payment;
import com.project.fondea.repository.DonationCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationCertificateService {

    private final DonationCertificateRepository donationCertificateRepository;

    public List<DonationCertificateDto> getMyCertificates(UUID sponsorId) {
        return donationCertificateRepository.findBySponsorId(sponsorId)
                .stream()
                .map(DonationCertificateMapper::toDto)
                .toList();
    }

    public DonationCertificateDto getById(UUID sponsorId, UUID certificateId) {
        var certificate = donationCertificateRepository.findById(certificateId)
                .orElseThrow(() -> new EntityNotFoundException("Certificado de donación no encontrado"));

        if (!certificate.getSponsor().getId().equals(sponsorId)) {
            throw new UnauthorizedActionException("No puedes ver este certificado");
        }

        return DonationCertificateMapper.toDto(certificate);
    }

    public DonationCertificateDto createFromPayment(Payment payment) {
        var pledge = payment.getPledge();

        if (donationCertificateRepository.existsByPledgeId(pledge.getId())) {
            throw new BusinessRuleException("Ya existe un certificado para este pledge");
        }

        var certificate = DonationCertificate.builder()
                .pledge(pledge)
                .sponsor(pledge.getSponsor())
                .fiscalName(pledge.getSponsor().getName())
                .fiscalId("N/A")
                .amount(payment.getAmount())
                .issuedAt(LocalDateTime.now())
                .pdfUrl("pending-generation")
                .build();

        return DonationCertificateMapper.toDto(
                donationCertificateRepository.save(certificate)
        );
    }
}