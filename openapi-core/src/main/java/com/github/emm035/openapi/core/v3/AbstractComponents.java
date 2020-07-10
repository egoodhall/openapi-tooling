package com.github.emm035.openapi.core.v3;

import com.github.emm035.openapi.core.v3.examples.Example;
import com.github.emm035.openapi.core.v3.parameters.Parameter;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.security.SecurityScheme;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.callbacks.Callback;
import com.github.emm035.openapi.core.v3.content.Header;
import com.github.emm035.openapi.core.v3.links.Link;
import com.github.emm035.openapi.core.v3.requests.RequestBody;
import com.github.emm035.openapi.core.v3.responses.Response;
import org.immutables.value.Value.Immutable;

import java.util.Map;

@OpenApiStyle
@Immutable
public abstract class AbstractComponents {
  public abstract Map<String, Referenceable<Schema>> getSchemas();
  public abstract Map<String, Referenceable<Response>> getResponses();
  public abstract Map<String, Referenceable<Parameter>> getParameters();
  public abstract Map<String, Referenceable<RequestBody>> getRequestBodies();
  public abstract Map<String, Referenceable<Example>> getExamples();
  public abstract Map<String, Referenceable<SecurityScheme>> getSecuritySchemes();
  public abstract Map<String, Referenceable<Header>> getHeaders();
  public abstract Map<String, Referenceable<Link>> getLinks();
  public abstract Map<String, Referenceable<Callback>> getCallbacks();
}
