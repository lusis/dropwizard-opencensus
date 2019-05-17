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

import io.dropwizard.setup.Environment;
import io.opencensus.contrib.http.servlet.OcHttpServletFilter;
import io.opencensus.contrib.http.util.HttpViews;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import javax.servlet.DispatcherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracingJerseyServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(TracingJerseyServer.class.getName());
  private final Environment environment;
  private String[] paths = new String[] {"/*"};

  public TracingJerseyServer(final Environment environment) {
    this.environment = Objects.requireNonNull(environment);
  }

  public TracingJerseyServer setPaths(String... paths) {
    this.paths = Objects.requireNonNull(Arrays.copyOf(paths, paths.length));
    return this;
  }

  public String[] getPaths() {
    return Arrays.copyOf(this.paths, this.paths.length);
  }

  public void build() {
    LOGGER.debug("Enabling tracing on paths {}", (Object) this.paths);
    this.environment
        .servlets()
        .addFilter(OcHttpServletFilter.class.getName(), new OcHttpServletFilter())
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, this.paths);
    HttpViews.registerAllViews();
  }
}
