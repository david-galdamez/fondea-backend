package com.project.fondea.controller;

import com.project.fondea.dto.campaign.CampaignReviewDto;
import com.project.fondea.dto.campaign.CampaignStatusDto;
import com.project.fondea.service.CampaignService;
import com.project.fondea.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/campaign")
@RequiredArgsConstructor
public class AdminCampaignController {
    private final CampaignService campaignService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<CampaignReviewDto>>> getPending() {
        var campaigns = campaignService.findPendingReview();
        return ResponseEntity.ok(ApiResponse.ok(campaigns, ""));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<CampaignStatusDto>> approve(@PathVariable UUID id) {
        var campaign = campaignService.approve(id);
        return ResponseEntity.ok(ApiResponse.ok(campaign, "Campaña aprobada"));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<CampaignStatusDto>> reject(@PathVariable UUID id) {
        var campaign = campaignService.reject(id);
        return ResponseEntity.ok(ApiResponse.ok(campaign, "Campaña rechazada"));
    }
}
