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
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.junit.ClassRule;
import org.junit.Test;

public class ExampleApplicationTest {

  @ClassRule
  public static final DropwizardAppRule<ExampleConfiguration> RULE =
      new DropwizardAppRule<>(
          ExampleApplication.class, ResourceHelpers.resourceFilePath("config.yml"));

  @Test
  public void clientURLWorks() throws Exception {
    Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

    Response response =
        client
            .target(String.format("http://localhost:%d/example/client", RULE.getLocalPort()))
            .request()
            .get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.readEntity(String.class)).isEqualTo("AlwaysSampleSampler\n");
  }
}
