/*
 * Copyright © 2019 John E Vincent (lusis.org+github.com@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.lusis.dropwizard.opencensus.exporters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter;
import java.util.Collection;

/**
 * DefaultExporter
 *
 * <p>The default exporter drops all spans to the ground and discards them
 */
@JsonTypeName("default")
public class DefaultExporter implements ExporterFactory {
  private static final DefaultExporter.NullExporterHandler HANDLER =
      new DefaultExporter.NullExporterHandler();

  /** Register this exporter with the {@link io.opencensus.trace.Tracer} */
  @Override
  public void register() {
    register(Tracing.getExportComponent().getSpanExporter());
  }

  /** Unregister this exporter with the {@link io.opencensus.trace.Tracer} */
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

  @Override
  @JsonIgnore
  public ExporterFactory getExporter() {
    return this;
  }

  static final class NullExporterHandler extends SpanExporter.Handler {
    NullExporterHandler() {}

    public void export(Collection<SpanData> spanDataList) {
      // drop to the floor
    }
  }
}
