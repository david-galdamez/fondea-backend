package com.project.fondea.export;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GoogleSheetsCampaignExporter implements CampaignExporter {

    private static final List<Object> HEADER = List.of(
            "id", "title", "creator", "category", "location", "goalAmount",
            "pendingPledged", "chargedPledged", "totalPledged", "pledgeCount",
            "deadline", "status", "featuredScore"
    );

    private final ObjectProvider<Sheets> sheetsProvider;

    @Value("${google.sheets.spreadsheet-id:}")
    private String spreadsheetId;

    @Value("${google.sheets.range:Campaigns!A1}")
    private String range;

    public GoogleSheetsCampaignExporter(ObjectProvider<Sheets> sheetsProvider) {
        this.sheetsProvider = sheetsProvider;
    }

    @Override
    public ExportFormat getFormat() {
        return ExportFormat.GOOGLE_SHEETS;
    }

    @Override
    public ExportResult export(List<CampaignExportRow> rows) {
        var sheets = sheetsProvider.getIfAvailable();
        if (sheets == null || spreadsheetId == null || spreadsheetId.isBlank()) {
            throw new IllegalStateException(
                    "La integración con Google Sheets no está configurada (revisa GOOGLE_SHEETS_ENABLED, "
                            + "GOOGLE_SHEETS_CREDENTIALS_PATH y GOOGLE_SHEETS_SPREADSHEET_ID).");
        }

        var values = new ArrayList<List<Object>>();
        values.add(HEADER);
        for (CampaignExportRow row : rows) {
            values.add(toCells(row));
        }

        try {
            sheets.spreadsheets().values()
                    .clear(spreadsheetId, sheetName(), new ClearValuesRequest())
                    .execute();

            sheets.spreadsheets().values()
                    .update(spreadsheetId, range, new ValueRange().setValues(values))
                    .setValueInputOption("RAW")
                    .execute();
        } catch (Exception e) {
            log.error("Error exportando campañas a Google Sheets: {}", e.getMessage());
            throw new IllegalStateException("No se pudo exportar a Google Sheets: " + e.getMessage(), e);
        }

        var url = "https://docs.google.com/spreadsheets/d/" + spreadsheetId;
        log.info("Exportadas {} campañas a Google Sheets ({})", rows.size(), url);
        return ExportResult.reference(url, "Campañas exportadas a Google Sheets");
    }

    private String sheetName() {
        return range.contains("!") ? range.substring(0, range.indexOf('!')) : range;
    }

    private List<Object> toCells(CampaignExportRow row) {
        return List.of(
                String.valueOf(row.id()),
                nullSafe(row.title()),
                nullSafe(row.creatorName()),
                nullSafe(row.categoryName()),
                nullSafe(row.locationCity()),
                String.valueOf(row.goalAmount()),
                String.valueOf(row.pendingPledged()),
                String.valueOf(row.chargedPledged()),
                String.valueOf(row.totalPledged()),
                String.valueOf(row.pledgeCount()),
                String.valueOf(row.deadline()),
                String.valueOf(row.status()),
                String.valueOf(row.featuredScore())
        );
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
