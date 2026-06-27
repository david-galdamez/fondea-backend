package com.project.fondea.dto.certificate;

import com.project.fondea.model.DonationCertificate;

public class DonationCertificateMapper {

    public static DonationCertificateDto toDto(DonationCertificate certificate) {
        return new DonationCertificateDto(
                certificate.getId(),
                certificate.getPledge().getId(),
                certificate.getPledge().getCampaign().getId(),
                certificate.getPledge().getCampaign().getTitle(),
                certificate.getSponsor().getName(),
                certificate.getSponsor().getEmail(),
                certificate.getFiscalName(),
                certificate.getFiscalId(),
                certificate.getAmount(),
                certificate.getIssuedAt(),
                certificate.getPdfUrl()
        );
    }
}