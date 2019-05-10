package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;

@FunctionalInterface
public interface OpenCensusConfiguration<C extends Configuration> {
    OpenCensusFactory getOpenCensusFactory(C configuration);
}

