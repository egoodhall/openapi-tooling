package com.github.emm035.openapi.core.v3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.emm035.openapi.core.v3.parameters.Parameter;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.HttpMethod;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.shared.Summarizable;
import com.google.common.collect.ImmutableMap;
import com.github.emm035.openapi.core.v3.servers.Server;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.immutables.value.Value.Check;

@Immutable
@OpenApiStyle
public abstract class AbstractPathItem implements Summarizable, Describable, Extensible {
  public abstract List<Server> getServers();
  public abstract List<Referenceable<Parameter>> getParameters();

  public abstract Optional<Operation> getGet();
  public abstract Optional<Operation> getPut();
  public abstract Optional<Operation> getPost();
  public abstract Optional<Operation> getPatch();
  public abstract Optional<Operation> getDelete();
  public abstract Optional<Operation> getOptions();
  public abstract Optional<Operation> getHead();
  public abstract Optional<Operation> getTrace();

  @Derived
  @JsonIgnore
  public synchronized Map<HttpMethod, Operation> getOperations() {
    ImmutableMap.Builder<HttpMethod, Operation> builder = ImmutableMap.builder();

    getGet().ifPresent(op -> builder.put(HttpMethod.GET, op));
    getPut().ifPresent(op -> builder.put(HttpMethod.PUT, op));
    getPost().ifPresent(op -> builder.put(HttpMethod.POST, op));
    getPatch().ifPresent(op -> builder.put(HttpMethod.PATCH, op));
    getDelete().ifPresent(op -> builder.put(HttpMethod.DELETE, op));
    getOptions().ifPresent(op -> builder.put(HttpMethod.OPTIONS, op));
    getHead().ifPresent(op -> builder.put(HttpMethod.HEAD, op));
    getTrace().ifPresent(op -> builder.put(HttpMethod.TRACE, op));

    return builder.build();
  }

  @Check
  AbstractPathItem normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return PathItem.builder()
      .from(this)
      .setExtensions(Checks.validExtensions(this))
      .build();
  }
}
