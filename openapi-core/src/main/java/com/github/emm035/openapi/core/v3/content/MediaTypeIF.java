package com.github.emm035.openapi.core.v3.content;

import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.OpenApiStyle;
import com.github.emm035.openapi.core.v3.shared.WithMultipleExamples;
import org.immutables.value.Value.Immutable;

@Immutable
@OpenApiStyle
public interface MediaTypeIF extends WithMultipleExamples, Extensible {
  Referenceable<Schema> getSchema();
}
