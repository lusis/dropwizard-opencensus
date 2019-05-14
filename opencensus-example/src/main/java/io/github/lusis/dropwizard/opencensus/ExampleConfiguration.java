package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExampleConfiguration extends Configuration {
    @NotNull
    @Valid
    public final OpenCensusFactory opencensus = new OpenCensusFactory();

    private final TracingJerseyClientConfiguration ocClient = new TracingJerseyClientConfiguration();

    @JsonProperty
    public OpenCensusFactory getOpenCensusFactory() {
        return opencensus;
    }

    @JsonProperty
    public TracingJerseyClientConfiguration getTracingJerseyClientConfiguration() {
        return ocClient;
    }
}
