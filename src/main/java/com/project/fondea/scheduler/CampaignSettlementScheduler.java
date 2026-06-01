package com.project.fondea.scheduler;

import com.project.fondea.service.CampaignSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CampaignSettlementScheduler {

    private final CampaignSettlementService campaignSettlementService;

    @Scheduled(cron = "0 0 2 * * *")
    public void settleExpiredCampaignsDaily() {
        campaignSettlementService.settleExpiredCampaigns();
    }
}