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

import static org.assertj.core.api.Assertions.assertThat;

import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration.Builder;
import io.opencensus.trace.AttributeValue;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

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
    assertThat(configuration.getFixedAttributes())
        .containsValue(AttributeValue.stringAttributeValue("bar"));
  }
}
