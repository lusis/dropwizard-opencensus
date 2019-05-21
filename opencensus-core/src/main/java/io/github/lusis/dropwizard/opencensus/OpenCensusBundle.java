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
package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;
import io.github.lusis.dropwizard.opencensus.server.TraceServlet;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.config.TraceParams;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OpenCensusBundle<C extends Configuration>
    implements ConfiguredBundle<C>, OpenCensusConfiguration<C> {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpenCensusBundle.class);
  private TraceConfig traceConfig;
  private TraceParams activeTraceParams;

  @Override
  public void initialize(Bootstrap<?> bootstrap) {}

  @Override
  public void run(final C configuration, Environment environment) throws Exception {
    final OpenCensusFactory ocFactory = getOpenCensusFactory(configuration);

    if (configuration == null) {
      LOGGER.info("OpenCensus tracing is disabled");
    } else {
      final boolean enabled = ocFactory.getEnabled();
      final SamplerFactory sampler = ocFactory.getSampler();
      final List<ExporterFactory> exporters = ocFactory.getExporters();
      final String[] paths = ocFactory.getPaths();
      final String propagationFormat = ocFactory.getPropagationFormat();
      final String isPublic = ocFactory.getIsPublic();

      if (enabled) {
        buildTracing(environment, sampler, exporters, propagationFormat, isPublic, paths);
      } else {
        LOGGER.info("OpenCensus tracing is disabled");
      }
    }
  }

  private void buildTracing(
      final Environment environment,
      final SamplerFactory sampler,
      final List<ExporterFactory> exporters,
      final String format,
      final String isPublic,
      String... paths) {
    LOGGER.info("Enabling opencensus tracing");
    if (exporters.isEmpty()) {
      LOGGER.warn("no exporters specified. using default {}", DefaultExporter.class.getName());
      exporters.add(new DefaultExporter());
    } else {
      traceConfig = Tracing.getTraceConfig();
      activeTraceParams = traceConfig.getActiveTraceParams();
      traceConfig.updateActiveTraceParams(
          activeTraceParams.toBuilder().setSampler(sampler.sampler()).build());
      LOGGER.info("exporters {}", exporters);
      for (ExporterFactory exporter : exporters) {
        exporter.register();
      }
    }

    LOGGER.info("TraceConfig {}", traceConfig.getActiveTraceParams());
    LOGGER.info("traced paths {}", (Object) paths);
    LOGGER.info("propagation format {}", format);
    LOGGER.info("public endpoints {}", isPublic);

    new TraceServlet(environment, format, isPublic, paths);
  }
}
