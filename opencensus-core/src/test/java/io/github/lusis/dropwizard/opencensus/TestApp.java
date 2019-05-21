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

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.github.lusis.dropwizard.opencensus.server.TraceServlet;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestApp extends Application<Configuration> {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestApp.class);

  @Override
  public void run(Configuration conf, Environment env) {
    LOGGER.info("starting test app");
    final String[] paths = new String[] {"/*"};
    Tracing.getTraceConfig()
        .updateActiveTraceParams(
            Tracing.getTraceConfig()
                .getActiveTraceParams()
                .toBuilder()
                .setSampler(Samplers.alwaysSample())
                .build());
    new TraceServlet(env.getApplicationContext(), env.servlets(), "b3", "false", paths);
    env.jersey().register(new TestResources());
  }
}
