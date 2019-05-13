package io.github.lusis.dropwizard.opencensus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import io.opencensus.trace.Tracing;
import javax.ws.rs.client.Client;
import java.util.Objects;


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
