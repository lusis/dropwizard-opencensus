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
package com.example.example.resources;

import io.opencensus.trace.Tracing;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/example")
public class ExampleResource {

  private final Client jerseyClient;

  public ExampleResource(Client client) {
    this.jerseyClient = Objects.requireNonNull(client);
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("ping")
  public String ping() {
    String myTracer = Tracing.getTraceConfig().getActiveTraceParams().getSampler().getDescription();
    return myTracer + "\n";
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("client")
  public Response client() throws InterruptedException {
    return jerseyClient.target("http://127.0.0.1:8080/example/ping").request().get();
  }
}
