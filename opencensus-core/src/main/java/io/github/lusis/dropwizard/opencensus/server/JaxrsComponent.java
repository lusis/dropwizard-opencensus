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

import io.opencensus.common.ExperimentalApi;
import io.opencensus.contrib.http.jaxrs.JaxrsContainerExtractor;
import io.opencensus.contrib.http.jaxrs.JaxrsContainerFilter;
import io.opencensus.contrib.http.util.HttpViews;
import io.opencensus.trace.Tracing;
import javax.annotation.Nullable;

/**
 * JaxrsComponent creates a JAX-RS Container request filter that can be applied
 *
 * <p>This is an alternate unused implementation due to the complexities of auto-wiring it when
 * creating the bundle
 */
@ExperimentalApi
public class JaxrsComponent {
  public JaxrsContainerFilter build(@Nullable String format) {
    HttpViews.registerAllServerViews();
    if (format != null && format.equalsIgnoreCase("b3")) {
      return new JaxrsContainerFilter(
          new JaxrsContainerExtractor(), Tracing.getPropagationComponent().getB3Format(), false);
    }
    return new JaxrsContainerFilter();
  }
}
