package io.github.lusis.dropwizard.opencensus.exporters;

import io.opencensus.exporter.trace.logging.LoggingTraceExporter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
