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

import static io.opencensus.contrib.http.servlet.OcHttpServletFilter.OC_PUBLIC_ENDPOINT;
import static io.opencensus.contrib.http.servlet.OcHttpServletFilter.OC_TRACE_PROPAGATOR;

import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;
import io.opencensus.contrib.http.servlet.OcHttpServletFilter;
import io.opencensus.contrib.http.util.HttpViews;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceServlet {
  private static final Logger LOGGER = LoggerFactory.getLogger(TraceServlet.class);

  public TraceServlet(
      @NotNull Environment environment, String format, String isPublic, String[] paths) {
    this(environment.getApplicationContext(), environment.servlets(), format, isPublic, paths);
  }

  public TraceServlet(
      @NotNull MutableServletContextHandler context,
      @NotNull ServletEnvironment servlets,
      String format,
      String isPublic,
      String[] paths) {

    context.setAttribute(OC_TRACE_PROPAGATOR, Format.getFormat(format));
    context.setInitParameter(OC_PUBLIC_ENDPOINT, isPublic);
    servlets
        .addFilter(OcHttpServletFilter.class.getName(), new OcHttpServletFilter())
        .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, paths);
    HttpViews.registerAllServerViews();
  }
}
