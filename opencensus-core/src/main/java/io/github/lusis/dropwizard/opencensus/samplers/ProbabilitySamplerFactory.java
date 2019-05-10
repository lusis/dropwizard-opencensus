package io.github.lusis.dropwizard.opencensus.samplers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("probability")
public class ProbabilitySamplerFactory extends AbstractSamplerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbabilitySamplerFactory.class);
    private double sampleRate = 0.1;

    @JsonProperty("sampleRate")
    public void setSampleRate(double rate) {
        this.sampleRate = rate;
    }

    @JsonProperty("sampleRate")
    public double getSampleRate() {
        return this.sampleRate;
    }

    @Override
    public Sampler sampler() {
        LOGGER.info("using sampler: probability with sample rate {}", getSampleRate());
        return Samplers.probabilitySampler(this.sampleRate);
    }
}
