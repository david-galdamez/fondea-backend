package com.project.fondea.service;

import com.project.fondea.dto.certificate.DonationCertificateDto;
import com.project.fondea.dto.certificate.DonationCertificateMapper;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.UnauthorizedActionException;
import com.project.fondea.repository.DonationCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}