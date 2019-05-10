package io.github.lusis.dropwizard.opencensus.samplers;

import com.fasterxml.jackson.annotation.JsonProperty;


import javax.annotation.Nullable;


public abstract class AbstractSamplerFactory implements SamplerFactory {
    private SamplerFactory sampler = null;

    @JsonProperty
    public void setSampler(SamplerFactory sampler) {
        this.sampler = sampler;
    }

    @JsonProperty
    @Nullable
    public SamplerFactory getSampler() {
        if (sampler == null) {
            return new DefaultSampleFactory();
        }
        return sampler;
    }
}
