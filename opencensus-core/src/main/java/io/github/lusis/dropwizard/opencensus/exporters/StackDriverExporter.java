package io.github.lusis.dropwizard.opencensus.exporters;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration.Builder;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.AttributeValue;

@JsonTypeName("stackdriver")
public class StackDriverExporter extends AbstractExporterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StackDriverExporter.class.getName());
    private String projectId = null;
    private Map<String, String> attributes = Collections.emptyMap();

    @JsonProperty("projectId")
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @JsonProperty("projectId")
    public String getProjectId() {
        return this.projectId;
    }

    @JsonProperty("attributes")
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void register() {
        registerStackDriver();
    }

    @Override
    public void unregister() {
        StackdriverTraceExporter.unregister();
    }

    @JsonIgnore
    protected void registerStackDriver() {
        try {
            Builder builder = createBuilder();
            StackdriverTraceExporter.createAndRegister(builder.build());
        } catch (Exception e) {
            LOGGER.error("Unable to register stackdriver exporter", e);
        }
    }

    @JsonIgnore
    protected Builder createBuilder() {
        Builder builder = StackdriverTraceConfiguration.builder();
        final String projectId = this.getProjectId();
        final Map<String, String> stringAttributes = this.getAttributes();

        if (projectId != null) {
            builder = builder.setProjectId(projectId);
        }
        if (stringAttributes != null) {
            Map<String, AttributeValue> stackDriverStringAttributes = stringAttributes.entrySet().stream()
                    .collect(toMap(e -> e.getKey(), e -> AttributeValue.stringAttributeValue(e.getValue())));
            builder = builder.setFixedAttributes(stackDriverStringAttributes);
        }
        return builder;
    }
}
