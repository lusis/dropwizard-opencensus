package io.github.lusis.dropwizard.opencensus;


import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporterFactory;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;
import io.github.lusis.dropwizard.opencensus.samplers.DefaultSampleFactory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;


public class OpenCensusFactory {
    private boolean enabled = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCensusFactory.class);


    @Nullable
    private SamplerFactory sampler = new DefaultSampleFactory();

    @Nullable
    private ExporterFactory exporter = new DefaultExporterFactory();

    @Nullable
    public boolean isEnabled() {
        return enabled;
    }

    @JsonProperty
    @Nullable
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    @Nullable
    public boolean getEnabled(boolean enabled) {return this.isEnabled();}

    @JsonProperty("sampler")
    @Nullable
    public SamplerFactory getSamplerFactory() {
        return this.sampler;
    }

    @JsonProperty("sampler")
    @Nullable
    public void setSamplerFactory(SamplerFactory sampler) {
        this.sampler = sampler;
    }

    @JsonProperty("exporter")
    @Nullable
    public ExporterFactory getExporterFactory() {
        return this.exporter;
    }

    @JsonProperty("exporter")
    @Nullable
    public void setExporterFactory(ExporterFactory exporter) {
        this.exporter = exporter;
    }
}