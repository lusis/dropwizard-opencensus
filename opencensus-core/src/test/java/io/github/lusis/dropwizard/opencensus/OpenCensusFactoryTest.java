package io.github.lusis.dropwizard.opencensus;


import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;



import io.github.lusis.dropwizard.opencensus.exporters.*;


public class OpenCensusFactoryTest {

    @Test
    public void isDiscoverable() throws Exception {
        // Make sure the types we specified in META-INF gets picked up
        assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
                .contains(LoggingExporterFactory.class);
    }


}