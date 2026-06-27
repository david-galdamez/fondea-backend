package com.project.fondea.controller;

import com.project.fondea.dto.certificate.DonationCertificateDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.DonationCertificateService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class DonationCertificateController {

    private final DonationCertificateService donationCertificateService;
    private final AuthContext authContext;

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<DonationCertificateDto>>> getMyCertificates(HttpServletRequest request) {
        var sponsorId = authContext.getCurrentUserId();
        var certificates = donationCertificateService.getMyCertificates(sponsorId);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        certificates,
                        "Certificados obtenidos correctamente",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonationCertificateDto>> getById(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var sponsorId = authContext.getCurrentUserId();
        var certificate = donationCertificateService.getById(sponsorId, id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        certificate,
                        "Certificado obtenido correctamente",
                        request.getRequestURI()
                )
        );
    }
}