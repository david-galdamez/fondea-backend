package com.project.fondea.export;

import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class CampaignExporterRegistry {

    private final Map<ExportFormat, CampaignExporter> exporters = new EnumMap<>(ExportFormat.class);

    public CampaignExporterRegistry(List<CampaignExporter> exporterBeans) {
        for (CampaignExporter exporter : exporterBeans) {
            exporters.put(exporter.getFormat(), exporter);
        }
    }

    public CampaignExporter get(ExportFormat format) {
        var exporter = exporters.get(format);
        if (exporter == null) {
            throw new IllegalArgumentException("No hay exportador para el formato: " + format);
        }
        return exporter;
    }
}
