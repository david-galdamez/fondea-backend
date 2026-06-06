package com.project.fondea.dto.withdrawal;

import com.project.fondea.model.WithdrawalRequest;

public class WithdrawalMapper {
    public static WithdrawalCreatedDto toCreated(WithdrawalRequest withdrawal) {
        return new WithdrawalCreatedDto(
                withdrawal.getId(),
                withdrawal.getGrossAmount(),
                withdrawal.getCommissionAmount(),
                withdrawal.getNetAmount(),
                withdrawal.getStatus(),
                withdrawal.getRequestedAt()
        );
    }

    public static WithdrawalDto toDto(WithdrawalRequest withdrawal) {
        return new WithdrawalDto(
                withdrawal.getId(),
                withdrawal.getCampaign().getId(),
                withdrawal.getCampaign().getTitle(),
                withdrawal.getGrossAmount(),
                withdrawal.getCommissionAmount(),
                withdrawal.getNetAmount(),
                withdrawal.getStatus(),
                withdrawal.getRequestedAt(),
                withdrawal.getPaidAt()
        );
    }
}
