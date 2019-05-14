package io.github.lusis.dropwizard.opencensus.exporters;

import io.opencensus.trace.Tracing;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collection;

@JsonTypeName("default")
public class DefaultExporter extends AbstractExporterFactory {
    private static final DefaultExporter.NullExporterHandler HANDLER = new DefaultExporter.NullExporterHandler();

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
