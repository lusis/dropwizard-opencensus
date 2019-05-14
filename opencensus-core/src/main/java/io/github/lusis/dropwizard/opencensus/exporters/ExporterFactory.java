package io.github.lusis.dropwizard.opencensus.exporters;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = LoggingExporter.class)
public interface ExporterFactory extends Discoverable {
    void register();

    void unregister();
}
