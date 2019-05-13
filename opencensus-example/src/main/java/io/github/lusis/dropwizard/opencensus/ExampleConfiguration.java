package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lusis.dropwizard.opencensus.OcClientBuilder;
import io.github.lusis.dropwizard.opencensus.OcClientConfiguration;

import io.github.lusis.dropwizard.opencensus.OpenCensusFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExampleConfiguration extends Configuration {
    @NotNull
    @Valid
    public final OpenCensusFactory opencensus = new OpenCensusFactory();

    private final OcClientConfiguration ocClient = new OcClientConfiguration();

    @JsonProperty
    public OpenCensusFactory getOpenCensusFactory() {
        return opencensus;
    }

    @JsonProperty
    public OcClientConfiguration getOcClientConfiguration() {
        return ocClient;
    }
}
