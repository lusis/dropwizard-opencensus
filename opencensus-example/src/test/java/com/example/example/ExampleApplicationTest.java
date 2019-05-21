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
package com.example.example;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.opencensus.testing.export.TestHandler;
import io.opencensus.trace.export.SpanData;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class ExampleApplicationTest {

  // we're going to register an additional exporter outside of our configuration
  private static final TestHandler testHandler = TestHelpers.getTestHandler();

  @BeforeClass
  public static void before() {
    TestHelpers.registerTestExporter();
  }

  @AfterClass
  public static void after() {
    TestHelpers.unregisterTestExporter();
  }

  @ClassRule
  public static final DropwizardAppRule<ExampleConfiguration> DEFAULT_CONFIG =
      new DropwizardAppRule<>(
          ExampleApplication.class, ResourceHelpers.resourceFilePath("config.yml"));

  @ClassRule
  public static final DropwizardAppRule<ExampleConfiguration> B3_CONFIG =
      new DropwizardAppRule<>(
          ExampleApplication.class, ResourceHelpers.resourceFilePath("b3-config.yml"));

  @Test
  public void basicConfig() throws Exception {
    Client client =
        new JerseyClientBuilder(DEFAULT_CONFIG.getEnvironment()).build("basic-config-test");

    Response response =
        client
            .target(
                String.format("http://localhost:%d/example/ping", DEFAULT_CONFIG.getLocalPort()))
            .request()
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo("AlwaysSampleSampler\n");
  }

  @Test
  public void testTraceParentPropagation() throws Exception {
    Client client =
        new JerseyClientBuilder(DEFAULT_CONFIG.getEnvironment()).build("traceparent-config-test");
    // we build our test server base url since the default url
    // for the query param is dropwizard's default port
    String baseUrl = String.format("http://localhost:%d/example/", DEFAULT_CONFIG.getLocalPort());
    Response response =
        client.target(baseUrl + "client").queryParam("url", baseUrl + "headers").request().get();
    assertThat(response.getStatus()).isEqualTo(200);
    String body = response.readEntity(String.class);
    assertThat(body).isNotEmpty();
    HashMap<String, List<String>> json = TestHelpers.headerJsonParser(body);

    // let's test that we're using w3c format by default
    // since that's the opencensus-java default
    assertThat(json.get("traceparent"))
        .isNotNull()
        .withFailMessage("We should have a traceparent header");
    String traceId = TestHelpers.getTraceIdFromTraceParent(json.get("traceparent").get(0));
    List<SpanData> spans = testHandler.waitForExport(3);
    List<SpanData> traceSpans = TestHelpers.getSpansForTrace(traceId, spans);
    assertThat(traceSpans.size())
        .isEqualTo(3)
        .withFailMessage("We should have three spans for this trace");
  }

  @Test
  public void testB3Propagation() throws Exception {
    Client client = new JerseyClientBuilder(B3_CONFIG.getEnvironment()).build("b3-config-test");
    String baseUrl = String.format("http://localhost:%d/example/", B3_CONFIG.getLocalPort());
    Response response =
        client.target(baseUrl + "client").queryParam("url", baseUrl + "headers").request().get();
    assertThat(response.getStatus()).isEqualTo(200);
    String body = response.readEntity(String.class);
    assertThat(body).isNotEmpty();

    HashMap<String, List<String>> results = TestHelpers.headerJsonParser(body);
    assertThat(results.get("X-B3-Sampled"))
        .isNotNull()
        .withFailMessage("we should have a b3 sampled header");
    assertThat(results.get("X-B3-SpanId"))
        .isNotNull()
        .withFailMessage("we should have a b3 spanid header");
    assertThat(results.get("X-B3-TraceId"))
        .isNotNull()
        .withFailMessage("we should have a b3 traceid header");

    // we set to always sample so sampled should be 1
    assertThat(results.get("X-B3-Sampled").get(0))
        .isEqualTo("1")
        .withFailMessage("trace should be sampled");

    String myTraceID = results.get("X-B3-TraceId").get(0);
    /*
     This should generate 3 spans for one traceid:
     - initial request
     - traced client started by initial request
     - client destination
    */
    List<SpanData> spans = testHandler.waitForExport(3);
    List<SpanData> traceSpans = TestHelpers.getSpansForTrace(myTraceID, spans);
    assertThat(traceSpans.size())
        .isEqualTo(3)
        .withFailMessage("we should have three spans for this trace");
  }
}
