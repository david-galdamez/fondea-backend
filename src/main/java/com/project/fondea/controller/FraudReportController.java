package com.project.fondea.controller;

import com.project.fondea.dto.fraud.CreateFraudReportRequest;
import com.project.fondea.dto.fraud.FraudReportDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.FraudReportService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud-reports")
@RequiredArgsConstructor
public class FraudReportController {

    private final FraudReportService fraudReportService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<FraudReportDto>> createReport(
            @Valid @RequestBody CreateFraudReportRequest createRequest,
            HttpServletRequest request
    ) {
        var reporterId = authContext.getCurrentUserId();
        var report = fraudReportService.createReport(reporterId, createRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(
                        report,
                        "Reporte de fraude creado correctamente",
                        request.getRequestURI()
                )
        );
    }
}
