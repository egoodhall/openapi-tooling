package com.github.emm035.openapi.annotation.processor.api.models;

import com.github.emm035.openapi.annotation.processor.api.SourceWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immutables.value.Value;

public interface BaseEndpoint<P, R> {
  Set<String> getProduces();
  Set<String> getConsumes();
  String getMethod();
  String getPath();
  List<P> getParameters();
  List<R> getResponses();

  @Value.Default
  default Map<String, ? extends SourceWriter> getExtensions() {
    return Collections.emptyMap();
  }
}
