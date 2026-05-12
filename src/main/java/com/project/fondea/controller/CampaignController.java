package com.project.fondea.controller;

import com.project.fondea.dto.campaign.CampaignDto;
import com.project.fondea.dto.campaign.RegisterCampaignRequest;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.AuthService;
import com.project.fondea.service.CampaignService;
import com.project.fondea.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/campaign")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;
    private final AuthContext context;

    @PostMapping
    public ResponseEntity<ApiResponse<CampaignDto>> registerCampaign(@Valid @RequestBody RegisterCampaignRequest registerRequest) {
        var userId = context.getCurrentUserId();

        var campaign = campaignService.create(userId, registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(campaign, "Campaña creada con exito"));
    }
}
