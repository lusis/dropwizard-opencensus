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
package io.github.lusis.dropwizard.opencensus.server;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.github.lusis.dropwizard.opencensus.TestApp;
import io.github.lusis.dropwizard.opencensus.TestHelpers;
import io.github.lusis.dropwizard.opencensus.client.TracingClientProvider;
import io.opencensus.testing.export.TestHandler;
import io.opencensus.trace.export.SpanData;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TracingServerTest {

  private static final TestHandler testHandler = TestHelpers.getTestHandler();
  public static final DropwizardTestSupport<Configuration> B3 =
      new DropwizardTestSupport<>(
          TestApp.class, null, ConfigOverride.config("server.applicationConnectors[0].port", "0"));

  @BeforeClass
  public static void before() {
    TestHelpers.registerTestExporter();
    B3.before();
  }

  @AfterClass
  public static void after() {
    B3.after();
    TestHelpers.unregisterTestExporter();
  }

  @Test
  public void testTrace() {
    Client client = new JerseyClientBuilder(B3.getEnvironment()).build("test client");
    Response response =
        client
            .target(String.format("http://localhost:%d/headers", B3.getLocalPort()))
            .request()
            .get();
    List<SpanData> spans = testHandler.waitForExport(1);
    assertThat(spans.size()).isEqualTo(1);
  }

  @Test
  public void testDistributedTrace() {
    Client client =
        new JerseyClientBuilder(B3.getEnvironment())
            .withProvider(new TracingClientProvider("b3").getFilter())
            .build("traced-client");
    Response response =
        client
            .target(String.format("http://localhost:%d/headers", B3.getLocalPort()))
            .request()
            .get();
    String body = response.readEntity(String.class);
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(body).isNotEmpty();

    HashMap<String, List<String>> results = TestHelpers.headerJsonParser(body);
    assertThat(results.get("X-B3-TraceId").get(0)).isNotNull();
    assertThat(results.get("X-B3-SpanId").get(0)).isNotNull();
    String myTraceID = results.get("X-B3-TraceId").get(0);

    List<SpanData> spans = testHandler.waitForExport(2);
    List<SpanData> traceSpans = TestHelpers.getSpansForTrace(myTraceID, spans);
    assertThat(traceSpans.size()).isEqualTo(2);
    SpanData childSpan = traceSpans.get(0);
    assertThat(childSpan.getHasRemoteParent()).isTrue();
  }
}
