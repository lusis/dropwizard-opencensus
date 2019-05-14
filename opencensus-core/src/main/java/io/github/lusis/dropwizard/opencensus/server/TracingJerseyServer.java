package io.github.lusis.dropwizard.opencensus.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dropwizard.setup.Environment;
import io.opencensus.contrib.http.servlet.OcHttpServletFilter;
import io.opencensus.contrib.http.util.HttpViews;

import java.util.EnumSet;
import java.util.Objects;

import javax.servlet.DispatcherType;

public class TracingJerseyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TracingJerseyServer.class.getName());
    private final Environment environment;
    private String[] paths = new String[]{"/*"};

    public TracingJerseyServer(final Environment environment) {
        this.environment = Objects.requireNonNull(environment);
    }

    public TracingJerseyServer setPaths(String... paths) {
        this.paths = Objects.requireNonNull(paths);
        return this;
    }

    public String[] getPaths() {
        return this.paths;
    }

    public void build() {
        LOGGER.debug("Enabling tracing on paths {}", (Object) this.paths);
        this.environment.servlets().addFilter(OcHttpServletFilter.class.getName(), new OcHttpServletFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, this.paths);
        HttpViews.registerAllViews();
    }
}
