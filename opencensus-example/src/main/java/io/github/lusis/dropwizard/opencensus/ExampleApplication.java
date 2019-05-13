package io.github.lusis.dropwizard.opencensus;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.github.lusis.dropwizard.opencensus.OpenCensusBundle;
import io.github.lusis.dropwizard.opencensus.OpenCensusFactory;
import io.github.lusis.dropwizard.opencensus.OcClientBuilder;
import io.github.lusis.dropwizard.opencensus.OcClientConfiguration;
import io.github.lusis.dropwizard.opencensus.resources.ExampleResource;

import javax.ws.rs.client.Client;

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
        bootstrap.addBundle(new OpenCensusBundle<ExampleConfiguration>() {
            @Override
            public OpenCensusFactory getOpenCensusFactory(ExampleConfiguration configuration) {
                return configuration.getOpenCensusFactory();
            }
        });
    }

    @Override
    public void run(final ExampleConfiguration configuration, final Environment environment) {
        final Client client;
        client = new OcClientBuilder(environment).build(configuration.getOcClientConfiguration());
        final ExampleResource resource = new ExampleResource(client);
        environment.jersey().register(resource);
    }

}