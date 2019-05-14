package io.github.lusis.dropwizard.opencensus.samplers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("probability")
public class ProbabilitySampler extends AbstractSamplerFactory {
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
        return Samplers.probabilitySampler(this.sampleRate);
    }
}
