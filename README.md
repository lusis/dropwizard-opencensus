Dropwizard OpenCensus Bundle
============================

A bundle for tracing via OpenCensus from Dropwizard applications.

Dependency Info
---------------

```xml
        <dependency>
            <groupId>io.github.lusis</groupId>
            <artifactId>opencensus-core</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

Usage
-----

Add `OpenCensusBundle` to your Application class:

```java
    @Override
    public void initialize(final Bootstrap<MyConfiguration> bootstrap) {
        bootstrap.addBundle(
                new OpenCensusBundle<ExampleConfiguration>() {
                    @Override
                    public OpenCensusFactory getOpenCensusFactory(ExampleConfiguration configuration) {
                        return configuration.getOpenCensusFactory();
                    }
                });
    }

```

Configuration
-------------

Out of the box, no configuration is required. The default is to not enable tracing.

If you want to configure options for development:

```yaml
opencensus:
  enabled: true
  exporter:
    type: logging
  sampler:
    type: always
```

Potential production kinds of settings might be:

```yaml
opencensus:
  enabled: true
  exporter:
    type: logging
  sampler:
    type: probability
    sampleRate: 0.25
```

For now the only valid exporter that actually exports data (as opposed to dropping it on the floor) is `logging`.
Valid `sampler` types are `always`, `never`, `probability`.
The default probability `sampleRate` is `0.10`.

The bundle will apply tracing to all non-admin paths via the `OcHttpServletFilter`.

Example Application
-------------------

This bundle includes a simple DropWizard application with one exposed path: `/example/ping`.

You can build and start the example application like so:

```
mvn clean package
java -jar opencensus-example/target/opencensus-example-0.0.1-SNAPSHOT.jar server opencensus-example/config.yml
```

From another window, you can `curl http://localhost:8080/example/ping`

In the window where you started the server you should see the trace data logged:

```
INFO  [2019-05-10 22:52:42,681] org.eclipse.jetty.server.AbstractConnector: Started application@41eb94bc{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
INFO  [2019-05-10 22:52:42,683] org.eclipse.jetty.server.AbstractConnector: Started admin@378cfecf{HTTP/1.1,[http/1.1]}{0.0.0.0:8081}
INFO  [2019-05-10 22:52:42,683] org.eclipse.jetty.server.Server: Started @1534ms
0:0:0:0:0:0:0:1 - - [10/May/2019:22:52:50 +0000] "GET /example/ping HTTP/1.1" 200 20 "-" "curl/7.54.0" 63
INFO  [2019-05-10 22:52:51,685] io.opencensus.exporter.trace.logging.LoggingTraceExporter: SpanData{context=SpanContext{traceId=TraceId{traceId=6ec5580662c2ead4601c309c89116daa}, spanId=SpanId{spanId=404b1a062605559d}, traceOptions=TraceOptions{sampled=true}}, parentSpanId=null, hasRemoteParent=null, name=/example/ping, kind=null, startTimestamp=Timestamp{seconds=1557528770, nanos=355005781}, attributes=Attributes{attributeMap={http.status_code=AttributeValueLong{longValue=200}, http.user_agent=AttributeValueString{stringValue=curl/7.54.0}, http.path=AttributeValueString{stringValue=/example/ping}, http.url=AttributeValueString{stringValue=http://localhost:8080/example/ping}, http.method=AttributeValueString{stringValue=GET}, http.host=AttributeValueString{stringValue=localhost}}, droppedAttributesCount=0}, annotations=TimedEvents{events=[], droppedEventsCount=0}, messageEvents=TimedEvents{events=[TimedEvent{timestamp=Timestamp{seconds=1557528770, nanos=395942352}, event=MessageEvent{type=SENT, messageId=1, uncompressedMessageSize=20, compressedMessageSize=0}}], droppedEventsCount=0}, links=Links{links=[], droppedLinksCount=0}, childSpanCount=0, status=Status{canonicalCode=OK, description=null}, endTimestamp=Timestamp{seconds=1557528770, nanos=402912953}}
```

TODO
----

There's a lot to do here to make this production ready:

- [ ] Clean up the code
- [ ] Flesh out tests
- [ ] Expose exporters OTHER than logging such as StackDriver
- [ ] Expose traced routes in configuration
- [ ] Actually publish this to central

Contributing
------------

I've not written Java in a very long time and in the process of writing this I had to learn DropWizard bundles, Jackson and some other things.
I would GREATLY welcome any and all help including turning this project over to someone else.
This was written as an MVP to prove out something for work.

I think there's good bones here in terms of concepts.