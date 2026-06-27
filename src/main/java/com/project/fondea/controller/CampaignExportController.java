package com.project.fondea.controller;

import com.project.fondea.export.ExportFormat;
import com.project.fondea.service.CampaignExportService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class CampaignExportController {

    private final CampaignExportService campaignExportService;

    @Value("${google.sheets.spreadsheet-id:}")
    private String spreadsheetId;

    @GetMapping(value = "/campaigns/csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportCampaignsCsv() {
        var result = campaignExportService.export(ExportFormat.CSV);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + result.filename()
                )
                .contentType(MediaType.parseMediaType(result.contentType()))
                .body(result.content());
    }

    @PostMapping("/campaigns/google-sheets")
    public ResponseEntity<ApiResponse<Map<String, String>>> exportCampaignsToGoogleSheets(
            HttpServletRequest request
    ) {
        campaignExportService.exportAsync(ExportFormat.GOOGLE_SHEETS);

        var url = spreadsheetId == null || spreadsheetId.isBlank()
                ? ""
                : "https://docs.google.com/spreadsheets/d/" + spreadsheetId;

        return ResponseEntity.accepted().body(
                ApiResponse.ok(
                        Map.of("spreadsheetUrl", url),
                        "Exportación a Google Sheets en proceso",
                        request.getRequestURI()
                )
        );
    }
}
