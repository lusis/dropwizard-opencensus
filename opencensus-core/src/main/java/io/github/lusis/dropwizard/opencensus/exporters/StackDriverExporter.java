package io.github.lusis.dropwizard.opencensus.exporters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@JsonTypeName("stackdriver")
public class StackDriverExporter extends AbstractExporterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StackDriverExporter.class.getName());
    private String projectId = "";

    @JsonProperty("projectId")
    public void setProjectId(String projectId) { this.projectId = projectId; }

    @JsonProperty("projectId")
    public String getProjectId() { return this.projectId; }

    @Override
    public void register() { registerStackDriver();}

    @Override
    public void unregister() { StackdriverTraceExporter.unregister(); }

    @JsonIgnore
    protected void registerStackDriver() {
        final String projectid = this.getProjectId();

        try {
            //if (this.gcpProjectId == null) {
            //    StackdriverTraceExporter.createAndRegister(
            //            StackdriverTraceConfiguration.builder().build()
            //    );
            //} else {
                StackdriverTraceExporter.createAndRegister(
                        StackdriverTraceConfiguration.builder()
                                .setProjectId(projectid)
                                .build()
                );
            //}
        } catch (Exception e) {
            LOGGER.error("Unable to register stackdriver exporter", e);
        }
    }
}
