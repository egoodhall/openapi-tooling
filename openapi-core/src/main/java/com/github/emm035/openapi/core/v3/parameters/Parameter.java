package com.github.emm035.openapi.core.v3.parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.shared.Deprecatable;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.WithMultipleExamples;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = "in")
@JsonSubTypes({
  @Type(value = PathParameter.class, name = "path"),
  @Type(value = QueryParameter.class, name = "query"),
  @Type(value = HeaderParameter.class, name = "header"),
  @Type(value = CookieParameter.class, name = "cookie")
})
@JsonDeserialize
public interface Parameter extends WithMultipleExamples, Deprecatable, Describable, Extensible, Referenceable<Parameter> {

  Location getIn();
  String getName();
  Optional<Boolean> getRequired();
  Referenceable<Schema> getSchema();
  Optional<Object> getDefault();

  static CookieParameter.Builder cookieBuilder() {
    return CookieParameter.builder();
  }

  static PathParameter.Builder pathBuilder() {
    return PathParameter.builder();
  }

  static QueryParameter.Builder queryBuilder() {
    return QueryParameter.builder();
  }

  static HeaderParameter.Builder headerBuilder() {
    return HeaderParameter.builder();
  }

  enum Location {
    QUERY,
    HEADER,
    PATH,
    COOKIE;

    @JsonCreator
    static Location fromString(@JsonProperty String rawValue) {
      return Location.valueOf(rawValue.toUpperCase());
    }

    @Override
    @JsonValue
    public String toString() {
      return name().toLowerCase();
    }
  }
}
