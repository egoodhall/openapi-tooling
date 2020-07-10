package com.github.emm035.openapi.schema.generator.internal;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;

public interface Generator {
  Referenceable<Schema> emit(boolean asReference) throws JsonMappingException;
  default Referenceable<Schema> emit() throws JsonMappingException {
    return emit(true);
  }
}
