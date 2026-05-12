package com.project.fondea.controller;

import com.project.fondea.dto.campaign.*;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.CampaignService;
import com.project.fondea.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;
    private final AuthContext context;

    @PostMapping
    public ResponseEntity<ApiResponse<CampaignCreatedDto>> registerCampaign(@Valid @RequestBody RegisterCampaignRequest registerRequest) {
        var userId = context.getCurrentUserId();

        var campaign = campaignService.create(userId, registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(campaign, "Campaña creada con exito"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CampaignSummaryDto>>> search(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) String keyword) {

        var campaigns = campaignService.search(categoryId, locationId, keyword);

        return ResponseEntity.ok(ApiResponse.ok(campaigns, ""));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Void>> sendToReview(@PathVariable("id")UUID campaignId) {
        var userId = context.getCurrentUserId();

        campaignService.sendForReview(campaignId, userId);

        return ResponseEntity.ok(ApiResponse.ok(null, "Campaña enviada a revisión exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignDetailDto>> getCampaignDetails(@PathVariable("id")UUID campaignId) {
       var campaign = campaignService.getCampaignDetails(campaignId);

       return ResponseEntity.ok(ApiResponse.ok(campaign, "Campaña obtenida con exito"));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<MyCampaignDto>>> getCampaignByCreator() {

        var userId = context.getCurrentUserId();

        var campaigns = campaignService.getCampaignsByUserId(userId);

        return ResponseEntity.ok(ApiResponse.ok(campaigns, "Campaña obtenida con exito"));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<CampaignSummaryDto>>> getFeatured() {
        var campaigns = campaignService.getFeatured();
        return ResponseEntity.ok(ApiResponse.ok(campaigns, ""));
    }

    @GetMapping("/drafts")
    public ResponseEntity<ApiResponse<List<CampaignDraftDto>>> getDrafts() {
        var userId = context.getCurrentUserId();
        var campaigns = campaignService.getDraftedCampaigns(userId);
        return ResponseEntity.ok(ApiResponse.ok(campaigns, ""));
    }

}
