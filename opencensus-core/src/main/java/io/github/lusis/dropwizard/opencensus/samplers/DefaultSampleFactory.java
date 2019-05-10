package io.github.lusis.dropwizard.opencensus.samplers;

import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("never")
public class DefaultSampleFactory extends AbstractSamplerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSampleFactory.class);

    @Override
    public Sampler sampler() {
        LOGGER.info("using sampler: never");
        return Samplers.neverSample();
    }

}
