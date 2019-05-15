package io.github.lusis.dropwizard.opencensus.exporters;

import org.junit.Test;

public class LoggingExporterTest {
    @Test
    public void testLifecycle() throws Exception {
        LoggingExporter exporter = new LoggingExporter();
        exporter.register();
        exporter.unregister();
    }
}