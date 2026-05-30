package com.project.fondea.controller;

import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.service.FraudReportService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<List<FraudReportDto>>> getPendingReports(HttpServletRequest request) {
        var reports = fraudReportService.getPendingReports();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        reports,
                        "Reportes de fraude obtenidos correctamente",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<ApiResponse<FraudReportDto>> reviewReport(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var report = fraudReportService.reviewReport(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        report,
                        "Reporte revisado correctamente",
                        request.getRequestURI()
                )
        );
    }
}