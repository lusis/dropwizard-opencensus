package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.setup.Environment;
import io.dropwizard.client.JerseyClientBuilder;

import io.opencensus.contrib.http.jaxrs.JaxrsClientExtractor;
import io.opencensus.trace.Tracing;
import io.opencensus.contrib.http.jaxrs.JaxrsClientFilter;

import javax.ws.rs.client.Client;

import java.util.Objects;

public class OcClientBuilder {
    private final Environment environment;

    public OcClientBuilder(final Environment environment) {
        this.environment = Objects.requireNonNull(environment);
    }

    public Client build(final OcClientConfiguration configuration) {
        final JerseyClientBuilder client = new JerseyClientBuilder(environment)
                .using(configuration);
        return build(client);
    }

    public Client build(final JerseyClientBuilder client) {
        return client.withProvider(new JaxrsClientFilter()
        ).build("instrumented-http-client");
    }
}