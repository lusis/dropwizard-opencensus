package io.github.lusis.dropwizard.opencensus.exporters;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration.Builder;
import io.opencensus.trace.AttributeValue;

public class StackDriverExporterTest {

    @Test
    public void testDefaults() throws Exception {
        StackDriverExporter exporter = new StackDriverExporter();
        // we have to set a project id as we don't have a credentials json to work with
        exporter.setProjectId("foo");

        Builder builder = exporter.createBuilder();
        StackdriverTraceConfiguration configuration = builder.build();
        assertThat(configuration.getProjectId()).isEqualTo("foo");
    }

    @Test
    public void testAttributes() throws Exception {
        StackDriverExporter exporter = new StackDriverExporter();
        // we have to set a project id as we don't have a credentials json to work with
        exporter.setProjectId("foo");

        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("foo", "bar");

        exporter.setAttributes(attributes);

        Builder builder = exporter.createBuilder();
        StackdriverTraceConfiguration configuration = builder.build();
        assertThat(configuration.getFixedAttributes()).containsKey("foo");
        assertThat(configuration.getFixedAttributes()).containsValue(AttributeValue.stringAttributeValue("bar"));
    }
}