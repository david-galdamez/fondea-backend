package com.project.fondea.controller;

import com.project.fondea.dto.withdrawal.WithdrawalDto;
import com.project.fondea.service.WithdrawalService;
import com.project.fondea.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/withdrawals")
@RequiredArgsConstructor
public class AdminWithdrawalController {

    private final WithdrawalService withdrawalService;

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<WithdrawalDto>>> getPending() {
        var withdrawals = withdrawalService.getPending();

        return ResponseEntity.ok(
                ApiResponse.ok(withdrawals, "Retiros pendientes obtenidos correctamente")
        );
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<WithdrawalDto>> approve(@PathVariable UUID id) {
        var withdrawal = withdrawalService.approve(id);

        return ResponseEntity.ok(
                ApiResponse.ok(withdrawal, "Retiro aprobado")
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<WithdrawalDto>> reject(@PathVariable UUID id) {
        var withdrawal = withdrawalService.reject(id);

        return ResponseEntity.ok(
                ApiResponse.ok(withdrawal, "Retiro rechazado")
        );
    }
}