/*
 * Copyright © 2019 John E Vincent (lusis.org+github.com@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.lusis.dropwizard.opencensus.exporters;

import static java.util.stream.Collectors.toMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration.Builder;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.AttributeValue;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StackDriverExporter exports all spans to StackDriver
 *
 * <p>This requires follows the configuration rules of all GCP related services being that
 * credentials are stored in a JSON file and that file is read via the
 * `GOOGLE_APPLICATION_CREDENTIALS` environment variable pointing to said file
 *
 * <p>Most, GCP credential files have the project id defined in them. As such, you shouldn't only
 * need to set the environment variable to be able to use this.
 *
 * <p>If, for some reason, you need to be explicit about the project id, you can set it on this
 * exporter
 *
 * <p>You can get a credentials file by creating a new Service Account in google with the `Cloud
 * Trace Agent` role
 *
 * <p>Additionally, you can set a list of {@link io.opencensus.trace.export.SpanData.Attributes} to
 * be sent globally for every sampled span.
 */
@JsonTypeName("stackdriver")
public class StackDriverExporter implements ExporterFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(StackDriverExporter.class.getName());
  private String projectId = null;
  private Map<String, String> attributes = Collections.emptyMap();

  @Override
  @JsonIgnore
  public ExporterFactory getExporter() {
    return this;
  }

  /**
   * Set the GCP project ID if, for some reason, it's not in your credentials file
   *
   * @param projectId the GCP project id to set explicitly
   */
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

  /**
   * A list of attributes in key/value format to be send along with every sampled span
   *
   * <p>A good example of an attribute might be something like a data center location (atl vs
   * richmond vs beijing or an environment (prod vs dev)
   *
   * <p>In the stackdriver UI you can filter trace searches on these attributes
   *
   * <p>Note that the servlet filters and jersey clients that are instrumented create their own
   * attributes
   *
   * @param attributes a list of static attributes to apply to all exported spans
   */
  @JsonProperty("attributes")
  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  /** Register this exporter with the {@link io.opencensus.trace.Tracer} */
  @Override
  public void register() {
    registerStackDriver();
  }

  /** Unregister this exporter with the {@link io.opencensus.trace.Tracer} */
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
      Map<String, AttributeValue> stackDriverStringAttributes =
          stringAttributes.entrySet().stream()
              .collect(
                  toMap(e -> e.getKey(), e -> AttributeValue.stringAttributeValue(e.getValue())));
      builder = builder.setFixedAttributes(stackDriverStringAttributes);
    }
    return builder;
  }
}
