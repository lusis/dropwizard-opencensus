package io.github.lusis.dropwizard.opencensus.exporters;

import io.opencensus.trace.Tracing;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("default")
public class DefaultExporterFactory extends AbstractExporterFactory {
    private static final DefaultExporterFactory.NullExporterHandler HANDLER = new DefaultExporterFactory.NullExporterHandler();

    @Override
    public void register() {
        register(Tracing.getExportComponent().getSpanExporter());
    }

    @Override
    public void unregister() {
        unregister(Tracing.getExportComponent().getSpanExporter());
    }

    static void register(SpanExporter spanExporter) {
        spanExporter.registerHandler("NullExporter", HANDLER);
    }

    static void unregister(SpanExporter spanExporter) {
        spanExporter.unregisterHandler("NullExporter");
    }

    static final class NullExporterHandler extends SpanExporter.Handler {
        NullExporterHandler() {

        }

        public void export(Collection<SpanData> spanDataList) {
            // drop to the floor
            return;
        }
    }

}
