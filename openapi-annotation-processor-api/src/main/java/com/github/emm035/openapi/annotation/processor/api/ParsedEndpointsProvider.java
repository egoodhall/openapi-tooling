package com.github.emm035.openapi.annotation.processor.api;

import com.github.emm035.openapi.annotation.processor.api.models.Endpoint;
import java.util.List;

public interface ParsedEndpointsProvider {
  List<Endpoint> get();
}
