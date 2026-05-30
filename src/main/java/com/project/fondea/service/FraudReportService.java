package com.project.fondea.service;

import com.project.fondea.dto.fraud.CreateFraudReportRequest;
import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.dto.fraud.FraudReportMapper;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.exception.FraudReportAlreadyExistsException;
import com.project.fondea.model.FraudReport;
import com.project.fondea.model.enums.CampaignStatus;
import com.project.fondea.model.enums.FraudReportStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.FraudReportRepository;
import com.project.fondea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudReportService {

    private final FraudReportRepository fraudReportRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public List<FraudReportDto> getPendingReports() {
        return fraudReportRepository.findByStatus(FraudReportStatus.PENDING)
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

    public FraudReportDto reviewReport(UUID reportId) {
        var report = fraudReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));

        if (report.getStatus() != FraudReportStatus.PENDING) {
            throw new BusinessRuleException("El reporte de fraude ya fue revisado");
        }

        report.setStatus(FraudReportStatus.REVIEWED);

        return FraudReportMapper.toDto(
                fraudReportRepository.save(report)
        );
    }

}
