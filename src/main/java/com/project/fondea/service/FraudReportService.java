package com.project.fondea.service;

import com.project.fondea.dto.fraud.CreateFraudReportRequest;
import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.dto.fraud.FraudReportMapper;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.FraudReportAlreadyExistsException;
import com.project.fondea.model.FraudReport;
import com.project.fondea.model.User;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.FraudReportStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.FraudReportRepository;
import com.project.fondea.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudReportService {

    private final FraudReportRepository fraudReportRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public FraudReportDto getById(UUID id) {
        var report = fraudReportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));
        return FraudReportMapper.toDto(report);
    }

    public List<FraudReportDto> getReportsByStatus(FraudReportStatus status) {
        if (status == null) {
            return fraudReportRepository.findAll()
                    .stream()
                    .map(FraudReportMapper::toDto)
                    .toList();
        }
        return fraudReportRepository.findByStatus(status)
                .stream()
                .map(FraudReportMapper::toDto)
                .toList();
    }

    public FraudReportDto createReport(UUID reporterId, CreateFraudReportRequest request) {

        var campaign = campaignRepository.findById(request.campaignId())
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new BusinessRuleException("Solo se pueden reportar campañas activas");
        }

        if (campaign.getCreator().getId().equals(reporterId)) {
            throw new BusinessRuleException("No puedes reportar tu propia campaña");
        }

        if (fraudReportRepository.existsByReporterIdAndCampaignId(
                reporterId,
                request.campaignId()
        )) {
            throw new FraudReportAlreadyExistsException(
                    reporterId,
                    request.campaignId()
            );
        }

        var reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        var report = FraudReport.builder()
                .reporter(reporter)
                .campaign(campaign)
                .reason(request.reason())
                .status(FraudReportStatus.PENDING)
                .build();

        return FraudReportMapper.toDto(
                fraudReportRepository.save(report)
        );
    }

    public FraudReportDto reviewReport(UUID reportId, User admin) {
        var report = getReportEntity(reportId);

        if (report.getStatus() != FraudReportStatus.PENDING) {
            throw new BusinessRuleException("El reporte de fraude no está pendiente");
        }

        report.setStatus(FraudReportStatus.REVIEWING);
        report.setReviewedBy(admin);

        return FraudReportMapper.toDto(
                fraudReportRepository.save(report)
        );
    }

    @Transactional
    public FraudReportDto resolveReport(UUID reportId, User admin, String notes) {
        var report = getReportEntity(reportId);

        if (report.getStatus() != FraudReportStatus.REVIEWING && report.getStatus() != FraudReportStatus.PENDING) {
            throw new BusinessRuleException("El reporte de fraude ya fue resuelto o descartado");
        }

        report.setStatus(FraudReportStatus.RESOLVED);
        report.setReviewedBy(admin);
        report.setResolutionNotes(notes);
        report.setResolvedAt(LocalDateTime.now());

        notificationService.create(
                report.getReporter(),
                report.getCampaign(),
                com.project.fondea.model.enums.NotificationType.FRAUD_REPORT_RESOLVED,
                "Tu reporte contra '" + report.getCampaign().getTitle() + "' fue resuelto."
        );

        return FraudReportMapper.toDto(
                fraudReportRepository.save(report)
        );
    }

    @Transactional
    public FraudReportDto dismissReport(UUID reportId, User admin, String notes) {
        var report = getReportEntity(reportId);

        if (report.getStatus() != FraudReportStatus.REVIEWING && report.getStatus() != FraudReportStatus.PENDING) {
            throw new BusinessRuleException("El reporte de fraude ya fue resuelto o descartado");
        }

        report.setStatus(FraudReportStatus.DISMISSED);
        report.setReviewedBy(admin);
        report.setResolutionNotes(notes);
        report.setResolvedAt(LocalDateTime.now());

        notificationService.create(
                report.getReporter(),
                report.getCampaign(),
                com.project.fondea.model.enums.NotificationType.FRAUD_REPORT_DISMISSED,
                "Tu reporte contra '" + report.getCampaign().getTitle() + "' fue descartado."
        );

        return FraudReportMapper.toDto(
                fraudReportRepository.save(report)
        );
    }

    private FraudReport getReportEntity(UUID reportId) {
        return fraudReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));
    }

}
