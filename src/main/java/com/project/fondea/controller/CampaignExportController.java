package com.project.fondea.controller;

import com.project.fondea.service.CampaignExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class CampaignExportController {

    private final CampaignExportService campaignExportService;

    @GetMapping(value = "/campaigns/csv", produces = "text/csv")
    public ResponseEntity<String> exportCampaignsCsv() {
        var csv = campaignExportService.exportCampaignsToCsv();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=campaigns.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}