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

import com.example.example.resources.ExampleResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.lusis.dropwizard.opencensus.OpenCensusBundle;
import io.github.lusis.dropwizard.opencensus.OpenCensusFactory;
import io.github.lusis.dropwizard.opencensus.TracingJerseyClient;
import javax.ws.rs.client.Client;

public class ExampleApplication extends Application<ExampleConfiguration> {

  public static void main(final String[] args) throws Exception {
    new ExampleApplication().run(args);
  }

  @Override
  public String getName() {
    return "dropwizard-opencensus-example";
  }

  @Override
  public void initialize(final Bootstrap<ExampleConfiguration> bootstrap) {
    bootstrap.addBundle(
        new OpenCensusBundle<ExampleConfiguration>() {
          @Override
          public OpenCensusFactory getOpenCensusFactory(ExampleConfiguration configuration) {
            return configuration.getOpenCensusFactory();
          }
        });
  }

  @Override
  public void run(final ExampleConfiguration configuration, final Environment environment) {
    final Client client =
        new TracingJerseyClient(environment)
            .build(configuration.getTracingJerseyClientConfiguration());
    // alternately, you can set the user_agent
    // client = new
    // TracingJerseyClient(environment).setName("my-custom-ua").build(configuration.getTracingJerseyClientConfiguration());
    final ExampleResource resource = new ExampleResource(client);
    environment.jersey().register(resource);
  }
}
