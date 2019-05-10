package io.github.lusis.dropwizard.opencensus.exporters;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;


public abstract class AbstractExporterFactory implements ExporterFactory {
    private ExporterFactory exporter;

    @JsonIgnore
    public void setExporter(@Nullable ExporterFactory exporter) {
        this.exporter = exporter;
    }

    @JsonIgnore
    public ExporterFactory getExporter() {
        return exporter;
    }
}
