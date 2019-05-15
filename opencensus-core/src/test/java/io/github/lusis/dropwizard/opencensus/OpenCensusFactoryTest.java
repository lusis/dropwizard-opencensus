package io.github.lusis.dropwizard.opencensus;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.LoggingExporter;
import io.github.lusis.dropwizard.opencensus.exporters.StackDriverExporter;

public class OpenCensusFactoryTest {

    @Test
    public void isDiscoverable() throws Exception {
        // Make sure the types we specified in META-INF gets picked up
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(LoggingExporter.class);
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(StackDriverExporter.class);
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes()).contains(DefaultExporter.class);
    }

}