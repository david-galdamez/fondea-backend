package com.project.fondea.dto.faq;

import com.project.fondea.model.CampaignFaq;

public class FaqMapper {
    public static FaqDto toDto(CampaignFaq faq) {
        return new FaqDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getAskedAt(),
                faq.getAnsweredAt()
        );
    }

    public static FaqManageDto toManageDto(CampaignFaq faq) {
        return new FaqManageDto(
                faq.getId(),
                faq.getAskedBy().getName(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getAskedAt(),
                faq.getAnswer() != null
        );
    }
}
