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
package io.github.lusis.dropwizard.opencensus.client;

import io.opencensus.contrib.http.jaxrs.JaxrsClientExtractor;
import io.opencensus.contrib.http.jaxrs.JaxrsClientFilter;
import io.opencensus.contrib.http.util.HttpViews;
import io.opencensus.trace.Tracing;
import javax.annotation.Nullable;

/**
 * TracingClientProvider provides a JAX-RS client filter that can be applied to a {@link
 * org.glassfish.jersey.client.JerseyClient} via {@link
 * io.dropwizard.client.JerseyClientBuilder#withProvider(Class)}
 *
 * <p>{@code Client client = new JerseyClientBuilder(B3.getEnvironment()) .withProvider(new
 * TracingClientProvider("b3").getFilter()) .build("traced-client"); }
 *
 * <p>to use the B3 trace propagation header format
 *
 * <p>{@code Client client = new JerseyClientBuilder(B3.getEnvironment()) .withProvider(new
 * TracingClientProvider().getFilter()) .build("traced-client"); }
 *
 * <p>to use the opencensus-java default of TraceContext propagation format
 */
public class TracingClientProvider {
  private JaxrsClientFilter filter = new JaxrsClientFilter();

  /**
   * Create a new TracingClientProvider with the specified format
   *
   * @param format use b3 or leave empty
   */
  public TracingClientProvider(@Nullable String format) {
    HttpViews.registerAllClientViews();
    if ((format != null) && (format.equalsIgnoreCase("b3"))) {
      filter =
          new JaxrsClientFilter(
              new JaxrsClientExtractor(), Tracing.getPropagationComponent().getB3Format());
    }
  }

  /** @return a filter that can be applied */
  public JaxrsClientFilter getFilter() {
    return filter;
  }
}
