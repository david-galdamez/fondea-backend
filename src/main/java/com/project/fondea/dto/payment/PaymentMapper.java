package com.project.fondea.dto.payment;

import com.project.fondea.model.Payment;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getPledge().getId(),
                payment.getPledge().getCampaign().getId(),
                payment.getPledge().getCampaign().getTitle(),
                payment.getPledge().getSponsor().getId(),
                payment.getPledge().getSponsor().getName(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getGatewayRef(),
                payment.getChargedAt()
        );
    }
}