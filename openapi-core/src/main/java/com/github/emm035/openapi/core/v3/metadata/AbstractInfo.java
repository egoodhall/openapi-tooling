package com.github.emm035.openapi.core.v3.metadata;

import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import java.util.Optional;
import org.immutables.value.Value.Check;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public abstract class AbstractInfo implements Extensible {

  public abstract String getTitle();

  public abstract Optional<String> getDescription();

  public abstract Optional<String> getTermsOfService();

  public abstract Optional<Contact> getContact();

  public abstract Optional<License> getLicense();

  public abstract String getVersion();

  @Check
  AbstractInfo normalizeExtensions() {
    if (Checks.allValid(this)) {
      return this;
    }
    return Info.builder().from(this).setExtensions(Checks.validExtensions(this)).build();
  }
}
