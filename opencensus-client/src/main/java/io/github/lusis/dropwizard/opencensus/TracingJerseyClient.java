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

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import io.opencensus.contrib.http.jaxrs.JaxrsClientFilter;
import java.util.Objects;
import javax.ws.rs.client.Client;

public class TracingJerseyClient {
  private final Environment environment;
  private String clientName = "opencensus-jersey-client";

  public TracingJerseyClient(final Environment environment) {
    this.environment = Objects.requireNonNull(environment);
  }

  public Client build(final TracingJerseyClientConfiguration configuration) {
    final JerseyClientBuilder client = new JerseyClientBuilder(environment).using(configuration);
    return build(client);
  }

  public TracingJerseyClient setName(final String clientName) {
    this.clientName = clientName;
    return this;
  }

  private Client build(final JerseyClientBuilder client) {
    return client.withProvider(new JaxrsClientFilter()).build(this.clientName);
  }
}
