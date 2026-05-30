package com.project.fondea.controller;

import com.project.fondea.dto.withdrawal.WithdrawalDto;
import com.project.fondea.service.WithdrawalService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiResponse<List<WithdrawalDto>>> getPending(HttpServletRequest request) {
        var withdrawals = withdrawalService.getPending();

        return ResponseEntity.ok(
                ApiResponse.ok(
                        withdrawals,
                        "Retiros pendientes obtenidos correctamente",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<WithdrawalDto>> approve(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var withdrawal = withdrawalService.approve(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        withdrawal,
                        "Retiro aprobado",
                        request.getRequestURI()
                )
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<WithdrawalDto>> reject(
            @PathVariable UUID id,
            HttpServletRequest request
    ) {
        var withdrawal = withdrawalService.reject(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        withdrawal,
                        "Retiro rechazado",
                        request.getRequestURI()
                )
        );
    }
}