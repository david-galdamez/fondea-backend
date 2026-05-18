package com.project.fondea.controller;

import com.project.fondea.model.FraudReport;
import com.project.fondea.service.FraudReportService;
import com.project.fondea.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/fraud-reports")
@RequiredArgsConstructor
public class AdminFraudReportController {

    private final FraudReportService fraudReportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FraudReport>>> getPendingReports() {
        var reports = fraudReportService.getPendingReports();

        return ResponseEntity.ok(
                ApiResponse.ok(reports, "Reportes de fraude obtenidos correctamente")
        );
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<ApiResponse<FraudReport>> reviewReport(@PathVariable UUID id) {
        var report = fraudReportService.reviewReport(id);

        return ResponseEntity.ok(
                ApiResponse.ok(report, "Reporte revisado correctamente")
        );
    }
}