package com.project.fondea.controller;

import com.project.fondea.dto.pledge.CreatePledgeRequest;
import com.project.fondea.dto.pledge.MyPledgeDto;
import com.project.fondea.dto.pledge.PledgeCreatedDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.PledgeService;
import com.project.fondea.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pledges")
@RequiredArgsConstructor
public class PledgeController {

    private final PledgeService pledgeService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<PledgeCreatedDto>> create(
            @Valid @RequestBody CreatePledgeRequest request) {

        var sponsorId = authContext.getCurrentUserId();
        var pledge = pledgeService.create(sponsorId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(pledge, "Pledge registrado exitosamente"));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<MyPledgeDto>>> getMyPledges() {

        var sponsorId = authContext.getCurrentUserId();
        var pledges = pledgeService.getMyPledges(sponsorId);

        return ResponseEntity.ok(ApiResponse.ok(pledges, ""));
    }
}