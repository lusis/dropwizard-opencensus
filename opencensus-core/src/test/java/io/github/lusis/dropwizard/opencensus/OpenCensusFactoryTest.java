package io.github.lusis.dropwizard.opencensus;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.LoggingExporter;
import io.github.lusis.dropwizard.opencensus.exporters.StackDriverExporter;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.configuration.YamlConfigurationFactory;
import com.google.common.io.Resources;
import javax.validation.Validator;
import java.io.File;

public class OpenCensusFactoryTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<OpenCensusFactory> validatingFactory =
            new YamlConfigurationFactory<>(OpenCensusFactory.class, validator, objectMapper, "dw");

    @Test
    public void isDiscoverable() throws Exception {
        // Make sure the types we specified in META-INF gets picked up
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(LoggingExporter.class);
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(StackDriverExporter.class);
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(DefaultExporter.class);
    }

    @Test
    public void disabledConfig() throws Exception {
        final File yml = new File(Resources.getResource("disabled.yaml").toURI());
        final OpenCensusFactory oc = validatingFactory.build(yml);
        assertThat((oc).getEnabled()).isFalse();
    }

    @Test
    public void enabledDefaultConfig() throws Exception {
        final File yml = new File(Resources.getResource("enabled.yaml").toURI());
        final OpenCensusFactory oc = validatingFactory.build(yml);
        assertThat((oc).getEnabled()).isTrue();
        // default exporter doesn't get assigned until run
        assertThat((oc).getExporters()).hasSize(0);
        assertThat((oc).getPaths()).contains("/*");
        assertThat((oc).getSamplerFactory().sampler().toString()).containsPattern("NeverSampleSampler");
    }

    @Test
    public void fullConfig() throws Exception {
        final File yml = new File(Resources.getResource("full.yaml").toURI());
        final OpenCensusFactory oc = validatingFactory.build(yml);
        assertThat((oc).getEnabled()).isTrue();
        assertThat((oc).getExporters()).hasSize(1);
        assertThat((oc).getPaths()).contains("/foo");
        assertThat((oc).getSamplerFactory().sampler().toString()).containsPattern("ProbabilitySampler\\{probability=0.75");
    }

    @Test
    public void alwaysSampleConfig() throws Exception {
        final File yml = new File(Resources.getResource("always_sample.yaml").toURI());
        final OpenCensusFactory oc = validatingFactory.build(yml);
        assertThat((oc).getEnabled()).isTrue();
        // default exporter doesn't get assigned until run
        assertThat((oc).getExporters()).hasSize(0);
        assertThat((oc).getPaths()).contains("/*");
        assertThat((oc).getSamplerFactory().sampler().toString()).containsPattern("AlwaysSampleSampler");
    }
}