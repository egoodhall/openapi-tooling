package com.github.emm035.openapi.core.v3;

import com.github.emm035.openapi.core.v3.parameters.Parameter;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.Deprecatable;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.shared.Summarizable;
import com.github.emm035.openapi.core.v3.metadata.ExternalDocumentation;
import com.github.emm035.openapi.core.v3.requests.RequestBody;
import com.github.emm035.openapi.core.v3.responses.Responses;
import com.github.emm035.openapi.core.v3.security.SecurityRequirement;
import com.github.emm035.openapi.core.v3.servers.Server;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Immutable
@OpenApiStyle
public abstract class AbstractOperation implements Summarizable, Describable, Extensible, Deprecatable {
  public abstract Responses getResponses();
  public abstract Optional<Referenceable<RequestBody>> getRequestBody();
  public abstract Optional<ExternalDocumentation> getExternalDocs();
  public abstract Optional<String> getOperationId();
  public abstract List<String> getTags();
  public abstract List<Referenceable<Parameter>> getParameters();
  public abstract List<Server> getServers();
  public abstract List<SecurityRequirement> getSecurity();
  // TODO: - Callbacks
  public abstract Map<String, Object> getCallbacks();

  @Check
  AbstractOperation normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Operation.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
