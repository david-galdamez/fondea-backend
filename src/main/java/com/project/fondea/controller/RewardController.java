package com.project.fondea.controller;

import com.project.fondea.dto.rewards.CreateRewardRequest;
import com.project.fondea.dto.rewards.RewardCreatedDto;
import com.project.fondea.dto.rewards.RewardDetailDto;
import com.project.fondea.dto.rewards.RewardSummaryDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.RewardsService;
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
@RequestMapping("/api/campaigns/{campaignId}/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardsService rewardService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<RewardCreatedDto>> create(
            @PathVariable UUID campaignId,
            @Valid @RequestBody CreateRewardRequest createRewardRequest,
            HttpServletRequest request
    ) {
        var creatorId = authContext.getCurrentUserId();
        var reward = rewardService.create(campaignId, creatorId, createRewardRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok(reward, "Recompensa creada con exito", request.getRequestURI())
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RewardSummaryDto>>> getAvailable(
            @PathVariable UUID campaignId,
            HttpServletRequest request
    ) {
        var rewards = rewardService.getAvailableByCampaign(campaignId);

        return ResponseEntity.ok(ApiResponse.ok(rewards, "", request.getRequestURI()));
    }

    @GetMapping("/manage")
    public ResponseEntity<ApiResponse<List<RewardDetailDto>>> getDetail(
            @PathVariable UUID campaignId,
            HttpServletRequest request
    ) {
        var creatorId = authContext.getCurrentUserId();
        var rewards = rewardService.getDetailsByCampaignId(campaignId, creatorId);

        return ResponseEntity.ok(ApiResponse.ok(rewards, "", request.getRequestURI()));
    }

    @DeleteMapping("/{rewardId}")
    public ResponseEntity<ApiResponse<Void>> delete(
           @PathVariable UUID campaignId,
           @PathVariable UUID rewardId,
           HttpServletRequest request
    ) {
        var creatorId = authContext.getCurrentUserId();
        rewardService.delete(rewardId, creatorId);

        return ResponseEntity.ok(ApiResponse.ok(null, "Recompensa eliminada con exito", request.getRequestURI()));
    }
}
