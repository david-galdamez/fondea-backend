package com.project.fondea.export;

public record ExportResult(
        byte[] content,
        String contentType,
        String filename,
        String resourceUrl,
        String message
) {
    public static ExportResult download(byte[] content, String contentType, String filename) {
        return new ExportResult(content, contentType, filename, null, null);
    }

    public static ExportResult reference(String resourceUrl, String message) {
        return new ExportResult(null, null, null, resourceUrl, message);
    }
}
