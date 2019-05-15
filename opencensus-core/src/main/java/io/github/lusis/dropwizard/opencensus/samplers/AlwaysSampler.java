package io.github.lusis.dropwizard.opencensus.samplers;

import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("always")
public class AlwaysSampler extends AbstractSamplerFactory {

    @Override
    public Sampler sampler() {
        return Samplers.alwaysSample();
    }

}
