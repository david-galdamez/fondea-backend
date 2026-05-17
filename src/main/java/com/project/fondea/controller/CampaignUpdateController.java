package com.project.fondea.controller;

import com.project.fondea.dto.campaignUpdate.CampaignUpdateCreatedDto;
import com.project.fondea.dto.campaignUpdate.CampaignUpdateDto;
import com.project.fondea.dto.campaignUpdate.CreateUpdateRequest;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.CampaignUpdateService;
import com.project.fondea.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/updates")
@RequiredArgsConstructor
public class CampaignUpdateController {

    private final CampaignUpdateService campaignUpdateService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<CampaignUpdateCreatedDto>> publish(
            @PathVariable UUID campaignId,
            @Valid @RequestBody CreateUpdateRequest request) {

        var creatorId = authContext.getCurrentUserId();
        var update = campaignUpdateService.publish(creatorId, campaignId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(update, "Actualización publicada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignUpdateDto>>> getUpdates(
            @PathVariable UUID campaignId) {

        var updates = campaignUpdateService.getByCampaign(campaignId);

        return ResponseEntity.ok(ApiResponse.ok(updates, ""));
    }
}