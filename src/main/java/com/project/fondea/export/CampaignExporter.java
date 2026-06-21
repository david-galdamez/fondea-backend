package com.project.fondea.export;

import java.util.List;

public interface CampaignExporter {

    ExportFormat getFormat();

    ExportResult export(List<CampaignExportRow> rows);
}
