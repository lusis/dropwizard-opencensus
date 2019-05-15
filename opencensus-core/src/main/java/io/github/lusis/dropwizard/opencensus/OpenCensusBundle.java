package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;

import io.github.lusis.dropwizard.opencensus.server.TracingJerseyServer;

import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class OpenCensusBundle<C extends Configuration>
        implements ConfiguredBundle<C>, OpenCensusConfiguration<C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCensusBundle.class);
    private static final TraceConfig traceConfig = Tracing.getTraceConfig();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(final C configuration, Environment environment) throws Exception {
        OpenCensusFactory ocFactory = getOpenCensusFactory(configuration);

        if (configuration == null) {
            LOGGER.info("OpenCensus tracing is disabled");
        } else {
            final boolean enabled = ocFactory.getEnabled();
            final SamplerFactory sampler = ocFactory.getSamplerFactory();
            final List<ExporterFactory> exporters = ocFactory.getExporters();
            final String[] paths = ocFactory.getPaths();
            if (enabled) {
                buildTracing(environment, sampler, exporters, paths);
            } else {
                LOGGER.info("OpenCensus tracing is disabled");
            }
        }
    }

    protected void buildTracing(final Environment environment, final SamplerFactory sampler,
            final List<ExporterFactory> exporters, String... paths) {
            traceConfig.updateActiveTraceParams(
                    traceConfig.getActiveTraceParams().toBuilder().setSampler(sampler.sampler()).build());
            LOGGER.info("Enabling opencensus tracing");
            LOGGER.info("sampler {}", sampler.getClass().getName());
            LOGGER.info("traced paths {}", (Object) paths);

            if ( exporters.isEmpty() ) {
                LOGGER.warn("no exporters specified. using default {}", DefaultExporter.class.getName());
                exporters.add(new DefaultExporter());
            } else {
                LOGGER.info("exporters {}", (Object) exporters);
                for (ExporterFactory exporter : exporters) {
                    exporter.register();
                }
            }
            new TracingJerseyServer(environment).setPaths(paths).build();
    }

}
