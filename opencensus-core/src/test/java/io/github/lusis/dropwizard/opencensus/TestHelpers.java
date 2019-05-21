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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import io.opencensus.testing.export.TestHandler;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.export.SpanData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestHelpers {
  private static final TestHandler testHandler = new TestHandler();

  public static void registerTestExporter() {
    Tracing.getExportComponent().getSpanExporter().registerHandler("test", testHandler);
  }

  public static void unregisterTestExporter() {
    Tracing.getExportComponent().getSpanExporter().unregisterHandler("test");
  }

  public static TestHandler getTestHandler() {
    return testHandler;
  }

  public static List<SpanData> getSpansForTrace(String traceId, List<SpanData> spans) {
    List<SpanData> wantedSpans = new ArrayList<>();

    for (SpanData span : spans) {
      if (span.getContext().getTraceId().toLowerBase16().equals(traceId)) {
        wantedSpans.add(span);
      }
    }
    return wantedSpans;
  }

  public static String getTraceIdFromTraceParent(String traceParentString) {
    List<String> parsed = Splitter.on("-").trimResults().splitToList(traceParentString);
    return parsed.get(1);
  }

  public static SpanData getSpanInList(String spanId, List<SpanData> spans) {
    for (SpanData span : spans) {
      if (span.getContext().getSpanId().toLowerBase16().equals(spanId)) {
        return span;
      }
    }
    return null;
  }

  public static SpanData getChildSpanFromParent(String parentSpanId, List<SpanData> spans) {
    for (SpanData span : spans) {
      if (span.getParentSpanId() != null
          && span.getParentSpanId().toLowerBase16().equals(parentSpanId)) {
        return span;
      }
    }
    return null;
  }

  public static HashMap<String, List<String>> headerJsonParser(String json) {
    TypeReference<HashMap<String, List<String>>> typeRef =
        new TypeReference<HashMap<String, List<String>>>() {};
    HashMap<String, List<String>> results = new HashMap<String, List<String>>();
    try {
      results = new ObjectMapper().readValue(json, typeRef);
    } catch (Exception e) {
      // noop
    }
    return results;
  }
}
