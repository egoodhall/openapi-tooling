package com.github.emm035.openapi.core.v3.security;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.Typed;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes({
  @Type(value = ApiKeyScheme.class, name = "apiKey"),
  @Type(value = HttpScheme.class, name = "http"),
  @Type(value = OAuth2Scheme.class, name = "oauth2"),
  @Type(value = OpenIdConnectScheme.class, name = "openIdConnect")
})
@JsonDeserialize
public interface SecurityScheme extends Extensible, Describable, Typed<SecurityScheme.Type>, Referenceable<SecurityScheme> {

  enum Type {
    API_KEY,
    HTTP,
    OAUTH2,
    OPEN_ID_CONNECT
    ;

    private static final Converter<String, String> TO_JSON_CASE_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
    private static final Converter<String, String> FROM_JSON_CASE_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);

    @JsonValue
    private String toJson() {
      return TO_JSON_CASE_CONVERTER.convert(name());
    }

    @JsonAnyGetter
    private static Type fromJson(@JsonProperty String rawValue) {
      return Type.valueOf(FROM_JSON_CASE_CONVERTER.convert(rawValue));
    }
  }
}
