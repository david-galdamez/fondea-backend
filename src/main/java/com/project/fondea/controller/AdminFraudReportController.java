package com.project.fondea.controller;

import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.model.enums.FraudReportStatus;
import com.project.fondea.service.FraudReportService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/fraud-reports")
@RequiredArgsConstructor
public class AdminFraudReportController {

    private final FraudReportService fraudReportService;
    private final AuthContext authContext;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FraudReportDto>>> getAll(
            @RequestParam(required = false) FraudReportStatus status,
            HttpServletRequest request
    ) {
        var reports = fraudReportService.getReportsByStatus(status);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        reports,
                        "Reportes de fraude obtenidos correctamente",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FraudReportDto>> getById(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var report = fraudReportService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        report,
                        "Reporte obtenido correctamente",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<ApiResponse<FraudReportDto>> reviewReport(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var admin = authContext.getCurrentUser();
        var report = fraudReportService.reviewReport(id, admin);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        report,
                        "Reporte marcado en revisión",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<FraudReportDto>> resolveReport(
            @PathVariable UUID id,
            @RequestBody(required = false) Map<String, String> body,
            HttpServletRequest request
    ) {
        var admin = authContext.getCurrentUser();
        var notes = body != null ? body.get("resolutionNotes") : null;
        var report = fraudReportService.resolveReport(id, admin, notes);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        report,
                        "Reporte resuelto",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/dismiss")
    public ResponseEntity<ApiResponse<FraudReportDto>> dismissReport(
            @PathVariable UUID id,
            @RequestBody(required = false) Map<String, String> body,
            HttpServletRequest request
    ) {
        var admin = authContext.getCurrentUser();
        var notes = body != null ? body.get("resolutionNotes") : null;
        var report = fraudReportService.dismissReport(id, admin, notes);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        report,
                        "Reporte descartado",
                        request.getRequestURI()
                )
        );
    }
}
