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
package io.github.lusis.dropwizard.opencensus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.lusis.dropwizard.opencensus.client.TracingClientProvider;
import io.github.lusis.dropwizard.opencensus.exporters.ExporterFactory;
import io.github.lusis.dropwizard.opencensus.samplers.DefaultSampler;
import io.github.lusis.dropwizard.opencensus.samplers.SamplerFactory;
import io.opencensus.contrib.http.jaxrs.JaxrsClientFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

public class OpenCensusFactory {
  private boolean enabled = true;
  private String[] paths = new String[] {"/*"};
  private String propagationFormat = null;
  private String isPublic = "false";

  /**
   * Enable opencensus tracing
   *
   * @param enabled enable tracing?
   */
  @JsonProperty
  @Nullable
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @JsonProperty
  @Nullable
  public Boolean getEnabled() {
    return this.enabled;
  }

  @JsonProperty
  @Nullable
  public String getPropagationFormat() {
    return this.propagationFormat;
  }

  @JsonProperty
  public String getIsPublic() {
    return this.isPublic;
  }

  /**
   * Set if endpoints served by the ApplicationContext are public internet endpoints are not
   *
   * <p>Determines if incoming trace propagation headers are treated as Parents or Links
   *
   * @param isPublic string of either true or false
   */
  @JsonProperty
  @Nullable
  public void setIsPublic(String isPublic) {
    this.isPublic = isPublic;
  }

  /**
   * Set the trace propagatation header format
   *
   * <p>valid options are to not set this or "b3"
   *
   * <p>This is only applied to the servlet context
   *
   * @param format trace propagation header format for endpoints
   */
  @JsonProperty
  @Nullable
  public void setPropagationFormat(String format) {
    this.propagationFormat = format;
  }

  @JsonProperty
  @Nullable
  public String[] getPaths() {
    return Arrays.copyOf(this.paths, this.paths.length);
  }

  /**
   * List of paths to apply tracing
   *
   * <p>By default all routes ("/*") are traced
   *
   * @param paths List of route paths to instrument
   */
  @JsonProperty
  @Nullable
  public void setPaths(String... paths) {
    this.paths = paths;
  }

  @JsonProperty("sampler")
  @Nullable
  private SamplerFactory sampler = new DefaultSampler();

  @JsonProperty("sampler")
  @Nullable
  public SamplerFactory getSampler() {
    return this.sampler;
  }

  @JsonProperty("exporters")
  @Nullable
  private List<ExporterFactory> exporters = new ArrayList<>();

  @JsonIgnore
  @Nullable
  public List<ExporterFactory> getExporters() {
    return this.exporters;
  }

  public JaxrsClientFilter toJaxRsProvider() {
    return new TracingClientProvider(this.getPropagationFormat()).getFilter();
  }
}
