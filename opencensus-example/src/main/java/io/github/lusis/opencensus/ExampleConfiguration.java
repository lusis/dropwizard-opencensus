package io.github.lusis.opencensus;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lusis.dropwizard.opencensus.OpenCensusFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExampleConfiguration extends Configuration {
    @NotNull
    @Valid
    public final OpenCensusFactory opencensus = new OpenCensusFactory();

    @JsonProperty
    public OpenCensusFactory getOpenCensusFactory() {
        return opencensus;
    }
}
