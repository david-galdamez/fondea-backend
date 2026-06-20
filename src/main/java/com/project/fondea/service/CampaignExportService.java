package com.project.fondea.service;

import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.PledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CampaignExportService {

    private final CampaignRepository campaignRepository;
    private final PledgeRepository pledgeRepository;

    public String exportCampaignsToCsv() {
        var campaigns = campaignRepository.findAll();

        var csv = new StringBuilder();

        csv.append("id,title,creator,category,location,goalAmount,pendingPledged,chargedPledged,totalPledged,pledgeCount,deadline,status,featuredScore\n");

        campaigns.forEach(campaign -> {
            var pendingPledged = sumPledges(campaign.getId(), PledgeStatus.PENDING);
            var chargedPledged = sumPledges(campaign.getId(), PledgeStatus.CHARGED);
            var totalPledged = pendingPledged.add(chargedPledged);

            var pledgeCount = pledgeRepository.countByCampaignId(campaign.getId());

            csv.append(campaign.getId()).append(",");
            csv.append(escape(campaign.getTitle())).append(",");
            csv.append(escape(campaign.getCreator().getName())).append(",");
            csv.append(escape(campaign.getCategory() != null ? campaign.getCategory().getName() : "")).append(",");
            csv.append(escape(campaign.getLocation() != null ? campaign.getLocation().getCity() : "")).append(",");
            csv.append(campaign.getGoalAmount()).append(",");
            csv.append(pendingPledged).append(",");
            csv.append(chargedPledged).append(",");
            csv.append(totalPledged).append(",");
            csv.append(pledgeCount).append(",");
            csv.append(campaign.getDeadline()).append(",");
            csv.append(campaign.getStatus()).append(",");
            csv.append(campaign.getFeaturedScore()).append("\n");
        });

        return csv.toString();
    }

    private BigDecimal sumPledges(java.util.UUID campaignId, PledgeStatus status) {
        return pledgeRepository.findByCampaignIdAndStatus(campaignId, status)
                .stream()
                .map(pledge -> pledge.getAmount() == null ? BigDecimal.ZERO : pledge.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}