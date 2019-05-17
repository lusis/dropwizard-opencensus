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

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.export.SpanData;
import io.opencensus.trace.export.SpanExporter;
import java.util.Collection;

@JsonTypeName("default")
public class DefaultExporter extends AbstractExporterFactory {
  private static final DefaultExporter.NullExporterHandler HANDLER =
      new DefaultExporter.NullExporterHandler();

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
    NullExporterHandler() {}

    public void export(Collection<SpanData> spanDataList) {
      // drop to the floor
    }
  }
}
