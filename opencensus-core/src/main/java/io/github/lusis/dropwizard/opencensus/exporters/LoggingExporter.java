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
import io.opencensus.exporter.trace.logging.LoggingTraceExporter;

@JsonTypeName("logging")
public class LoggingExporter implements ExporterFactory {

  @Override
  public void register() {
    LoggingTraceExporter.register();
  }

  @Override
  public void unregister() {
    LoggingTraceExporter.unregister();
  }

  @Override
  @JsonIgnore
  public ExporterFactory getExporter() {
    return this;
  }
}
