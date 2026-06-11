package com.project.fondea.controller;

import com.project.fondea.dto.campaign.CampaignDetailDto;
import com.project.fondea.dto.campaign.CampaignReviewDto;
import com.project.fondea.dto.campaign.CampaignStatusDto;
import com.project.fondea.dto.campaign.CampaignSummaryDto;
import com.project.fondea.dto.campaign.RejectCampaignRequest;
import com.project.fondea.service.CampaignService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/campaigns")
@RequiredArgsConstructor
public class AdminCampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignSummaryDto>>> getAll(HttpServletRequest request) {
        var campaigns = campaignService.getAll();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        campaigns,
                        "Campañas obtenidas correctamente",
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<CampaignReviewDto>>> getPending(HttpServletRequest request) {
        var campaigns = campaignService.findPendingReview();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        campaigns,
                        "Campañas pendientes obtenidas correctamente",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<CampaignDetailDto>> approve(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var campaign = campaignService.approve(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        campaign,
                        "Campaña aprobada",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<CampaignDetailDto>> reject(
            @PathVariable UUID id,
            @RequestBody(required = false) RejectCampaignRequest body,
            HttpServletRequest request
    ) {
        var reason = body != null ? body.rejectionReason() : null;
        var campaign = campaignService.reject(id, reason);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        campaign,
                        "Campaña rechazada",
                        request.getRequestURI()
                )
        );
    }
}
