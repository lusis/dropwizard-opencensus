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
package io.github.lusis.dropwizard.opencensus.samplers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.opencensus.trace.Sampler;
import io.opencensus.trace.samplers.Samplers;

/**
 * AlwaysSampler sets the sampling decision to {@link io.opencensus.trace.samplers
 * AlwaysSampleSampler}
 *
 * <p>This is really only useful for development as this will sample every single trace generated
 */
@JsonTypeName("always")
public class AlwaysSampler extends AbstractSamplerFactory {

  @Override
  public Sampler sampler() {
    return Samplers.alwaysSample();
  }
}
