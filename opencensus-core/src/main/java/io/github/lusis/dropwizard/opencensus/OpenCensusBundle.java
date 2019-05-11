package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;

import io.opencensus.contrib.http.servlet.OcHttpServletFilter;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;

import java.util.EnumSet;

public abstract class OpenCensusBundle<C extends Configuration>
        implements ConfiguredBundle<C>, OpenCensusConfiguration<C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCensusBundle.class);
    private static final TraceConfig traceConfig = Tracing.getTraceConfig();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(final C configuration, Environment environment) throws Exception {
        if (configuration == null) {
            return;
        }
        final boolean enabled = getOpenCensusFactory(configuration).isEnabled();
        final SamplerFactory sampler = getOpenCensusFactory(configuration).getSamplerFactory();
        final ExporterFactory exporter = getOpenCensusFactory(configuration).getExporterFactory();

        buildTracing(environment, enabled, sampler, exporter);
    }

    protected void buildTracing(final Environment environment, final boolean enabled, final SamplerFactory sampler,
            final ExporterFactory exporter) {
        if (!enabled) {
            LOGGER.info("OpenCensus tracing is disabled");
            return;
        } else {
            traceConfig.updateActiveTraceParams(
                    traceConfig.getActiveTraceParams().toBuilder().setSampler(sampler.sampler()).build());
            LOGGER.info("sampler {}", traceConfig.getActiveTraceParams().getSampler().getDescription());
            exporter.register();

            environment.servlets().addFilter(OcHttpServletFilter.class.getName(), new OcHttpServletFilter())
                    .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        }
    }
}
