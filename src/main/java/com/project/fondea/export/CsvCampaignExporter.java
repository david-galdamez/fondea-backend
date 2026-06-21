package com.project.fondea.export;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvCampaignExporter implements CampaignExporter {

    private static final String HEADER =
            "id,title,creator,category,location,goalAmount,pendingPledged,chargedPledged,"
                    + "totalPledged,pledgeCount,deadline,status,featuredScore";

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.CSV;
    }

    @Override
    public ExportResult export(List<CampaignExportRow> rows) {
        var csv = new StringBuilder();
        csv.append(HEADER).append("\n");

        for (CampaignExportRow row : rows) {
            csv.append(row.id()).append(",");
            csv.append(escape(row.title())).append(",");
            csv.append(escape(row.creatorName())).append(",");
            csv.append(escape(row.categoryName())).append(",");
            csv.append(escape(row.locationCity())).append(",");
            csv.append(row.goalAmount()).append(",");
            csv.append(row.pendingPledged()).append(",");
            csv.append(row.chargedPledged()).append(",");
            csv.append(row.totalPledged()).append(",");
            csv.append(row.pledgeCount()).append(",");
            csv.append(row.deadline()).append(",");
            csv.append(row.status()).append(",");
            csv.append(row.featuredScore()).append("\n");
        }

        return ExportResult.download(
                csv.toString().getBytes(StandardCharsets.UTF_8),
                "text/csv",
                "campaigns.csv"
        );
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
