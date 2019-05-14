package io.github.lusis.dropwizard.opencensus.samplers;

import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("always")
public class AlwaysSampleFactory extends AbstractSamplerFactory {

    @Override
    public Sampler sampler() {
        return Samplers.alwaysSample();
    }

}
