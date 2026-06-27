package com.project.fondea.export;

import com.project.fondea.model.Campaign;
import com.project.fondea.model.enums.PledgeStatus;
import com.project.fondea.repository.CampaignRepository;
import com.project.fondea.repository.PledgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignExportDataProvider {

    private final CampaignRepository campaignRepository;
    private final PledgeRepository pledgeRepository;

    @Transactional(readOnly = true)
    public List<CampaignExportRow> getRows() {
        var aggregates = loadAggregates();

        return campaignRepository.findAllForExport().stream()
                .map(campaign -> toRow(
                        campaign,
                        aggregates.getOrDefault(campaign.getId(), new PledgeAggregate())
                ))
                .toList();
    }

    private Map<UUID, PledgeAggregate> loadAggregates() {
        Map<UUID, PledgeAggregate> byCampaign = new HashMap<>();

        for (Object[] row : pledgeRepository.aggregateAmountsByCampaignAndStatus()) {
            var campaignId = (UUID) row[0];
            var status = (PledgeStatus) row[1];
            var sum = (BigDecimal) row[2];
            var count = (Long) row[3];

            byCampaign.computeIfAbsent(campaignId, key -> new PledgeAggregate())
                    .add(status, sum, count);
        }

        return byCampaign;
    }

    private CampaignExportRow toRow(Campaign campaign, PledgeAggregate aggregate) {
        var pending = aggregate.amountFor(PledgeStatus.PENDING);
        var charged = aggregate.amountFor(PledgeStatus.CHARGED);

        return new CampaignExportRow(
                campaign.getId(),
                campaign.getTitle(),
                campaign.getCreator() != null ? campaign.getCreator().getName() : "",
                campaign.getCategory() != null ? campaign.getCategory().getName() : "",
                campaign.getLocation() != null ? campaign.getLocation().getCity() : "",
                campaign.getGoalAmount(),
                pending,
                charged,
                pending.add(charged),
                aggregate.totalCount(),
                campaign.getDeadline(),
                campaign.getStatus(),
                campaign.getFeaturedScore()
        );
    }

    private static final class PledgeAggregate {
        private final Map<PledgeStatus, BigDecimal> amounts = new EnumMap<>(PledgeStatus.class);
        private long totalCount = 0;

        void add(PledgeStatus status, BigDecimal sum, long count) {
            amounts.merge(status, sum == null ? BigDecimal.ZERO : sum, BigDecimal::add);
            totalCount += count;
        }

        BigDecimal amountFor(PledgeStatus status) {
            return amounts.getOrDefault(status, BigDecimal.ZERO);
        }

        long totalCount() {
            return totalCount;
        }
    }
}
