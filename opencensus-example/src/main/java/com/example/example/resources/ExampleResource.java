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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opencensus.trace.Tracing;
import java.util.Objects;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

@Path("/example")
public class ExampleResource {

  private Client jerseyClient;

  public ExampleResource(Client client) {
    this.jerseyClient = Objects.requireNonNull(client);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("headers")
  public Response headers(@Context HttpHeaders headers) {
    MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
    try {
      String json = new ObjectMapper().writeValueAsString(requestHeaders);
      return Response.ok().entity(json + "\n").build();
    } catch (Exception e) {
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("ping")
  public String ping() {
    final String myTracer =
        Tracing.getTraceConfig().getActiveTraceParams().getSampler().getDescription();
    return myTracer + "\n";
  }

  @GET
  @Path("client")
  public Response urlGrabber(
      @DefaultValue("http://localhost:8080/example/headers") @QueryParam("url") String url)
      throws InterruptedException {
    if (url == null || url.trim().length() == 0) {
      return Response.serverError().entity("url cannot be blank").build();
    }
    final WebTarget target = this.jerseyClient.target(url);
    return target.request().get();
  }
}
