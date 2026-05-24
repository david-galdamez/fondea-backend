package com.project.fondea.controller;

import com.project.fondea.dto.withdrawal.CreateWithdrawalRequest;
import com.project.fondea.dto.withdrawal.WithdrawalCreatedDto;
import com.project.fondea.dto.withdrawal.WithdrawalDto;
import com.project.fondea.filter.AuthContext;
import com.project.fondea.service.WithdrawalService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
@RequiredArgsConstructor
public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final AuthContext authContext;

    @PostMapping
    public ResponseEntity<ApiResponse<WithdrawalCreatedDto>> requestWithdrawal(
            @Valid @RequestBody CreateWithdrawalRequest createWithdrawalRequest,
            HttpServletRequest request) {

        var creatorId = authContext.getCurrentUserId();
        var withdrawal = withdrawalService.requestWithdrawal(creatorId, createWithdrawalRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(withdrawal, "Solicitud de retiro creada exitosamente", request.getRequestURI()));
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<WithdrawalDto>>> getMyWithdrawals(
            HttpServletRequest request
    ) {

        var creatorId = authContext.getCurrentUserId();
        var withdrawals = withdrawalService.getMyWithdrawals(creatorId);

        return ResponseEntity.ok(ApiResponse.ok(withdrawals, "", request.getRequestURI()));
    }
}