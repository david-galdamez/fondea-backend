package com.project.fondea.dto.fraud;

import com.project.fondea.model.FraudReport;

public class FraudReportMapper {

    public static FraudReportDto toDto(FraudReport fraudReport) {
        return new FraudReportDto(
                fraudReport.getId(),
                fraudReport.getReporter().getId(),
                fraudReport.getReporter().getName(),
                fraudReport.getReporter().getEmail(),
                fraudReport.getCampaign().getId(),
                fraudReport.getCampaign().getTitle(),
                fraudReport.getReason(),
                fraudReport.getStatus(),
                fraudReport.getCreatedAt()
        );
    }
}