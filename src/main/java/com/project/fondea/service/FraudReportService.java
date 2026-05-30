package com.project.fondea.service;

import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.dto.fraud.FraudReportMapper;
import com.project.fondea.exception.BusinessRuleException;
import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.model.enums.FraudReportStatus;
import com.project.fondea.repository.FraudReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudReportService {

    private final FraudReportRepository fraudReportRepository;

    public List<FraudReportDto> getPendingReports() {
        return fraudReportRepository.findByStatus(FraudReportStatus.PENDING)
                .stream()
                .map(FraudReportMapper::toDto)
                .toList();
    }

    public FraudReportDto reviewReport(UUID reportId) {
        var report = fraudReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));

        if (report.getStatus() != FraudReportStatus.PENDING) {
            throw new BusinessRuleException("El reporte de fraude ya fue revisado");
        }

        report.setStatus(FraudReportStatus.REVIEWED);

        return FraudReportMapper.toDto(fraudReportRepository.save(report));
    }
}