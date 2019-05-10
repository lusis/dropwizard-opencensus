package io.github.lusis.dropwizard.opencensus.exporters;


import io.opencensus.exporter.trace.logging.LoggingTraceExporter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonTypeName("logging")
public class LoggingExporterFactory extends AbstractExporterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingExporterFactory.class);

    @Override
    public void register(){
        LOGGER.info("Registering LoggingTraceExporter");
        LoggingTraceExporter.register();
    }

    @Override
    public void unregister(){
        LOGGER.info("Unregistering LoggingTraceExporter");
        LoggingTraceExporter.unregister();
    }
}
