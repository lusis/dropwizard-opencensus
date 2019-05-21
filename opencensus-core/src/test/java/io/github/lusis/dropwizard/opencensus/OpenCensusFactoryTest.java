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

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.DiscoverableSubtypeResolver;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.github.lusis.dropwizard.opencensus.exporters.DefaultExporter;
import io.github.lusis.dropwizard.opencensus.exporters.LoggingExporter;
import io.github.lusis.dropwizard.opencensus.exporters.StackDriverExporter;
import io.github.lusis.dropwizard.opencensus.samplers.ProbabilitySampler;
import io.opencensus.testing.export.TestHandler;
import io.opencensus.trace.samplers.Samplers;
import java.io.File;
import javax.validation.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OpenCensusFactoryTest {
  private final ObjectMapper objectMapper = Jackson.newObjectMapper();
  private final Validator validator = Validators.newValidator();
  private final YamlConfigurationFactory<OpenCensusFactory> validatingFactory =
      new YamlConfigurationFactory<>(OpenCensusFactory.class, validator, objectMapper, "dw");
  private static final TestHandler testHandler = TestHelpers.getTestHandler();

  @Before
  public void before() {
    TestHelpers.registerTestExporter();
  }

  @After
  public void after() {
    TestHelpers.unregisterTestExporter();
  }

  @Test
  public void isDiscoverable() throws Exception {
    // Make sure the types we specified in META-INF gets picked up
    assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
        .contains(LoggingExporter.class);
    assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
        .contains(StackDriverExporter.class);
    assertThat(new DiscoverableSubtypeResolver().getDiscoveredSubtypes())
        .contains(DefaultExporter.class);
  }

  @Test
  public void disabledConfig() throws Exception {
    final File yml = new File(Resources.getResource("disabled.yaml").toURI());
    final OpenCensusFactory oc = validatingFactory.build(yml);
    assertThat(oc.getEnabled()).isFalse();
  }

  @Test
  public void enabledDefaultConfig() throws Exception {
    final File yml = new File(Resources.getResource("enabled.yaml").toURI());
    final OpenCensusFactory oc = validatingFactory.build(yml);
    assertThat(oc.getEnabled()).isTrue();
    assertThat(oc.getExporters()).hasSize(0);
    assertThat(oc.getPaths()).contains("/*");
    assertThat(oc.getSampler().sampler()).isInstanceOf(Samplers.neverSample().getClass());
  }

  @Test
  public void fullConfig() throws Exception {
    final File yml = new File(Resources.getResource("full.yaml").toURI());
    final OpenCensusFactory oc = validatingFactory.build(yml);
    final ProbabilitySampler pbSampler = (ProbabilitySampler) oc.getSampler();
    assertThat(oc.getEnabled()).isTrue();
    assertThat(oc.getExporters()).hasSize(1);
    assertThat(oc.getExporters().get(0).getExporter()).isInstanceOf(LoggingExporter.class);
    assertThat(oc.getPaths()).contains("/foo");
    assertThat(oc.getSampler().sampler())
        .isInstanceOf(Samplers.probabilitySampler(pbSampler.getSampleRate()).getClass());
    assertThat(oc.getSampler().sampler().toString())
        .contains("probability=" + pbSampler.getSampleRate());
  }

  @Test
  public void alwaysSampleConfig() throws Exception {
    final File yml = new File(Resources.getResource("always_sample.yaml").toURI());
    final OpenCensusFactory oc = validatingFactory.build(yml);
    assertThat(oc.getEnabled()).isTrue();
    assertThat(oc.getExporters()).hasSize(0);
    assertThat(oc.getPaths()).contains("/*");
    assertThat(oc.getSampler().sampler()).isInstanceOf(Samplers.alwaysSample().getClass());
  }

  @Test
  public void testB3Format() throws Exception {
    final File yml = new File(Resources.getResource("b3-config.yaml").toURI());
    final OpenCensusFactory oc = validatingFactory.build(yml);

    assertThat(oc.getPropagationFormat()).isEqualToIgnoringCase("b3");
  }
}
