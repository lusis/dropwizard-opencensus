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

public class TracingClientProvider {
  private JaxrsClientFilter filter = new JaxrsClientFilter();

  public TracingClientProvider(@Nullable String format) {
    HttpViews.registerAllClientViews();
    if ((format != null) && (format.equalsIgnoreCase("b3"))) {
      filter =
          new JaxrsClientFilter(
              new JaxrsClientExtractor(), Tracing.getPropagationComponent().getB3Format());
    }
  }

  public JaxrsClientFilter getFilter() {
    return filter;
  }
}
