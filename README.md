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

If you want to use an instrumented jersey client, you can as well via the opencensus-client package:

```xml
        <dependency>
            <groupId>io.github.lusis</groupId>
            <artifactId>opencensus-client</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
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

From another window, you can `curl http://localhost:8080/example/client`

In the window where you started the server you should see the trace data logged:

```
127.0.0.1 - - [13/May/2019:21:03:33 +0000] "GET /example/ping HTTP/1.1" 200 20 "-" "dropwizard-opencensus-example (instrumented-http-client)" 21
0:0:0:0:0:0:0:1 - - [13/May/2019:21:03:33 +0000] "GET /example/client HTTP/1.1" 200 20 "-" "curl/7.54.0" 156
INFO  [2019-05-13 21:03:35,331] io.opencensus.exporter.trace.logging.LoggingTraceExporter: SpanData{context=SpanContext{traceId=TraceId{traceId=c34898bd2a5a00cec1aa00ff5b41f14f}, spanId=SpanId{spanId=70bdcb2bdb09e7ce}, traceOptions=TraceOptions{sampled=true}}, parentSpanId=SpanId{spanId=0e3c6731e1853260}, hasRemoteParent=true, name=/example/ping, kind=SERVER, startTimestamp=Timestamp{seconds=1557781413, nanos=933000720}, attributes=Attributes{attributeMap={http.status_code=AttributeValueLong{longValue=200}, http.user_agent=AttributeValueString{stringValue=dropwizard-opencensus-example (instrumented-http-client)}, http.path=AttributeValueString{stringValue=/example/ping}, http.url=AttributeValueString{stringValue=http://127.0.0.1:8080/example/ping?null}, http.method=AttributeValueString{stringValue=GET}, http.host=AttributeValueString{stringValue=127.0.0.1}}, droppedAttributesCount=0}, annotations=TimedEvents{events=[], droppedEventsCount=0}, messageEvents=TimedEvents{events=[TimedEvent{timestamp=Timestamp{seconds=1557781413, nanos=947603892}, event=MessageEvent{type=SENT, messageId=1, uncompressedMessageSize=20, compressedMessageSize=0}}], droppedEventsCount=0}, links=Links{links=[], droppedLinksCount=0}, childSpanCount=0, status=Status{canonicalCode=OK, description=null}, endTimestamp=Timestamp{seconds=1557781413, nanos=952120465}}
INFO  [2019-05-13 21:03:35,331] io.opencensus.exporter.trace.logging.LoggingTraceExporter: SpanData{context=SpanContext{traceId=TraceId{traceId=c34898bd2a5a00cec1aa00ff5b41f14f}, spanId=SpanId{spanId=0e3c6731e1853260}, traceOptions=TraceOptions{sampled=true}}, parentSpanId=SpanId{spanId=61916035e34a579e}, hasRemoteParent=false, name=/example/ping, kind=CLIENT, startTimestamp=Timestamp{seconds=1557781413, nanos=873125449}, attributes=Attributes{attributeMap={http.path=AttributeValueString{stringValue=/example/ping}, http.url=AttributeValueString{stringValue=http://127.0.0.1:8080/example/ping}, http.status_code=AttributeValueLong{longValue=200}, http.method=AttributeValueString{stringValue=GET}, http.host=AttributeValueString{stringValue=127.0.0.1}}, droppedAttributesCount=0}, annotations=TimedEvents{events=[], droppedEventsCount=0}, messageEvents=TimedEvents{events=[], droppedEventsCount=0}, links=Links{links=[], droppedLinksCount=0}, childSpanCount=0, status=Status{canonicalCode=OK, description=null}, endTimestamp=Timestamp{seconds=1557781413, nanos=952089000}}
INFO  [2019-05-13 21:03:35,331] io.opencensus.exporter.trace.logging.LoggingTraceExporter: SpanData{context=SpanContext{traceId=TraceId{traceId=c34898bd2a5a00cec1aa00ff5b41f14f}, spanId=SpanId{spanId=61916035e34a579e}, traceOptions=TraceOptions{sampled=true}}, parentSpanId=null, hasRemoteParent=null, name=/example/client, kind=SERVER, startTimestamp=Timestamp{seconds=1557781413, nanos=810006345}, attributes=Attributes{attributeMap={http.status_code=AttributeValueLong{longValue=200}, http.user_agent=AttributeValueString{stringValue=curl/7.54.0}, http.path=AttributeValueString{stringValue=/example/client}, http.url=AttributeValueString{stringValue=http://localhost:8080/example/client?null}, http.method=AttributeValueString{stringValue=GET}, http.host=AttributeValueString{stringValue=localhost}}, droppedAttributesCount=0}, annotations=TimedEvents{events=[], droppedEventsCount=0}, messageEvents=TimedEvents{events=[TimedEvent{timestamp=Timestamp{seconds=1557781413, nanos=954954280}, event=MessageEvent{type=SENT, messageId=1, uncompressedMessageSize=20, compressedMessageSize=0}}], droppedEventsCount=0}, links=Links{links=[], droppedLinksCount=0}, childSpanCount=1, status=Status{canonicalCode=OK, description=null}, endTimestamp=Timestamp{seconds=1557781413, nanos=955030494}}
```

If you look closely, the span flows look like this:

- Initial request to `/example/client` has spanId `61916035e34a579e`
- client calls `http://localhost:8080/example/ping` with spanId `0e3c6731e1853260` and parent spanId `61916035e34a579e`
- request to `/example/ping` has spanId `70bdcb2bdb09e7ce` with parent spanId `0e3c6731e1853260`

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