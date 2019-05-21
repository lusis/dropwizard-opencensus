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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opencensus.trace.Tracing;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/")
public class TestResources {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("headers")
  public Response headers(@Context HttpHeaders headers) {
    MultivaluedMap<String, String> requestHeaders = headers.getRequestHeaders();
    try {
      String json = new ObjectMapper().writeValueAsString(requestHeaders);
      return Response.ok().entity(json).build();
    } catch (Exception e) {
      return Response.serverError().entity(e.getMessage()).build();
    }
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("traceconfig")
  public String traceconfig() {
    final String myTracer =
        Tracing.getTraceConfig().getActiveTraceParams().getSampler().getDescription();
    return myTracer;
  }
}
