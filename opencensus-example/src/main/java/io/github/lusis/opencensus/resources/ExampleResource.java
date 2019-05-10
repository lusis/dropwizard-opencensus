package io.github.lusis.opencensus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import io.opencensus.trace.Tracing;

@Path("/example")
public class ExampleResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("ping")
    public String ping() {
        String myTracer = Tracing.getTraceConfig().getActiveTraceParams().getSampler().getDescription();
        return myTracer + "\n";
    }
}
