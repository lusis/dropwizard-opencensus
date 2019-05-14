package io.github.lusis.dropwizard.opencensus.samplers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

import io.opencensus.trace.Sampler;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = DefaultSampler.class)
public interface SamplerFactory extends Discoverable {

    Sampler sampler();
}
