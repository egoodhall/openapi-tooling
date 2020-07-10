package com.github.emm035.openapi.core.v3.examples;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.shared.Describable;
import com.github.emm035.openapi.core.v3.shared.Extensible;
import com.github.emm035.openapi.core.v3.shared.Summarizable;

@JsonDeserialize
public interface Example
  extends Summarizable, Describable, Extensible, Referenceable<Example> {}
