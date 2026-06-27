package com.project.fondea.controller;

import com.project.fondea.dto.PageableResponse;
import com.project.fondea.dto.campaign.*;
import com.project.fondea.dto.pledge.CampaignPledgeDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.CampaignService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<CampaignCreatedDto>> registerCampaign(@Valid @RequestBody RegisterCampaignRequest registerRequest, HttpServletRequest request) {
        var userId = context.getCurrentUserId();

        var campaign = campaignService.create(userId, registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(campaign, "Campaña creada con exito", request.getRequestURI()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignCreatedDto>> updateCampaign(
            @PathVariable("id") UUID campaignId,
            @Valid @RequestBody RegisterCampaignRequest updateRequest,
            HttpServletRequest request) {
        var userId = context.getCurrentUserId();
        var campaign = campaignService.update(campaignId, userId, updateRequest);
        return ResponseEntity.ok(
                ApiResponse.ok(campaign, "Campaña actualizada con éxito", request.getRequestURI())
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CampaignSummaryDto>>> search(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request) {

        var campaigns = campaignService.search(categoryId, locationId, keyword);

        return ResponseEntity.ok(ApiResponse.ok(campaigns, "", request.getRequestURI()));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Void>> sendToReview(@PathVariable("id")UUID campaignId, HttpServletRequest request) {
        var userId = context.getCurrentUserId();

        campaignService.sendForReview(campaignId, userId);

        return ResponseEntity.ok(ApiResponse.ok(null, "Campaña enviada a revisión exitosamente", request.getRequestURI()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignDetailDto>> getCampaignDetails(@PathVariable("id")UUID campaignId, HttpServletRequest request) {
       var campaign = campaignService.getCampaignDetails(campaignId);

       return ResponseEntity.ok(ApiResponse.ok(campaign, "Campaña obtenida con exito", request.getRequestURI()));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<MyCampaignDto>>> getCampaignByCreator(HttpServletRequest request) {

        var userId = context.getCurrentUserId();

        var campaigns = campaignService.getCampaignsByUserId(userId);

        return ResponseEntity.ok(ApiResponse.ok(campaigns, "Campaña obtenida con exito", request.getRequestURI()));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<CampaignSummaryDto>>> getFeatured(@RequestParam(defaultValue = "6")int limit,HttpServletRequest request) {
        var campaigns = campaignService.getFeatured(limit);
        return ResponseEntity.ok(ApiResponse.ok(campaigns, "", request.getRequestURI()));
    }

    @GetMapping("/drafts")
    public ResponseEntity<ApiResponse<List<CampaignDraftDto>>> getDrafts(HttpServletRequest request) {
        var userId = context.getCurrentUserId();
        var campaigns = campaignService.getDraftedCampaigns(userId);
        return ResponseEntity.ok(ApiResponse.ok(campaigns, "", request.getRequestURI()));
    }

    // En CampaignController
    @GetMapping("/{campaignId}/pledges")
    public ResponseEntity<ApiResponse<PageableResponse<CampaignPledgeDto>>> getPledges(
            @PathVariable UUID campaignId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        var creatorId = context.getCurrentUserId();
        var pledges = campaignService.getByCampaign(campaignId, creatorId, page, size);

        return ResponseEntity.ok(ApiResponse.ok(pledges, "", request.getRequestURI()));
    }

}
