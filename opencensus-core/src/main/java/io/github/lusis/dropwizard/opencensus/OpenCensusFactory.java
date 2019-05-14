package io.github.lusis.dropwizard.opencensus;

import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;
import io.github.lusis.dropwizard.opencensus.samplers.DefaultSampler;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class OpenCensusFactory {
    private boolean enabled = true;
    private String[] paths = new String[]{"/*"};

    @Nullable
    private SamplerFactory sampler = new DefaultSampler();

    @Nullable
    private ExporterFactory exporter = new DefaultExporter();

    @JsonProperty
    @Nullable
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    @Nullable
    public boolean getEnabled() {
        return this.enabled;
    }

    @JsonProperty
    @Nullable
    public String[] getPaths() { return this.paths;};

    @JsonProperty
    @Nullable
    public void setPaths(String... paths) { this.paths = paths;};

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