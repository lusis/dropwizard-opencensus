package io.github.lusis.dropwizard.opencensus.exporters;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.opencensus.exporter.trace.logging.LoggingTraceExporter;

@JsonTypeName("logging")
public class LoggingExporter extends AbstractExporterFactory {

    @Override
    public void register() {
        LoggingTraceExporter.register();
    }

    @Override
    public void unregister() {
        LoggingTraceExporter.unregister();
    }
}
