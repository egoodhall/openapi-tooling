package com.github.emm035.openapi.annotation.processor.api;

import com.github.emm035.openapi.annotation.processor.api.models.ParsedEndpoint;
import javax.lang.model.element.Element;

public interface EndpointExtension {
  ParsedEndpoint modify(ParsedEndpoint endpoint, Element element);
}
