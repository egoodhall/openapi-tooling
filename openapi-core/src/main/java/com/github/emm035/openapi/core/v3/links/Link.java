package com.github.emm035.openapi.core.v3.links;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.emm035.openapi.core.v3.references.Referenceable;

@JsonDeserialize(using = LinkDeserializer.class)
public interface Link extends Referenceable<Link> {
}
