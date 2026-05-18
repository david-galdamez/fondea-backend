package com.project.fondea.service;

import com.project.fondea.exception.EntityNotFoundException;
import com.project.fondea.model.FraudReport;
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

    public List<FraudReport> getPendingReports() {
        return fraudReportRepository.findByStatus(FraudReportStatus.PENDING);
    }

    public FraudReport reviewReport(UUID reportId) {
        var report = fraudReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte de fraude no encontrado"));

        report.setStatus(FraudReportStatus.REVIEWED);

        return fraudReportRepository.save(report);
    }
}