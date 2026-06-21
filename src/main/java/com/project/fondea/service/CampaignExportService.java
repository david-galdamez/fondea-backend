package com.project.fondea.service;

import com.project.fondea.export.CampaignExportDataProvider;
import com.project.fondea.export.CampaignExporterRegistry;
import com.project.fondea.export.ExportFormat;
import com.project.fondea.export.ExportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignExportService {

    private final CampaignExportDataProvider dataProvider;
    private final CampaignExporterRegistry exporterRegistry;

    public ExportResult export(ExportFormat format) {
        var rows = dataProvider.getRows();
        return exporterRegistry.get(format).export(rows);
    }

    @Async
    public void exportAsync(ExportFormat format) {
        try {
            export(format);
        } catch (Exception e) {
            log.error("Falló la exportación asíncrona a {}: {}", format, e.getMessage());
        }
    }
}
