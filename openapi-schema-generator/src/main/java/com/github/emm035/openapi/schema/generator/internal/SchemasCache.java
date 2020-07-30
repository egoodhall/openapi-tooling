package com.github.emm035.openapi.schema.generator.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.emm035.openapi.core.v3.references.Ref;
import com.github.emm035.openapi.core.v3.references.Referenceable;
import com.github.emm035.openapi.core.v3.schemas.Schema;
import com.github.emm035.openapi.schema.generator.annotations.RefPrefix;
import com.github.emm035.openapi.schema.generator.exceptions.SchemaGenerationException;
import com.github.emm035.openapi.schema.generator.internal.annotations.CachedSchemas;
import com.github.emm035.openapi.schema.generator.internal.annotations.DefaultSchemas;
import com.github.emm035.openapi.schema.generator.internal.annotations.Internal;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.inject.Inject;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchemasCache {
  private static final Pattern REF_PATTERN = Pattern.compile(
    "\"\\$ref\":\\s*\"([^\"]+\\/)?(?<name>[^\"]+)\""
  );
  private final Map<String, Schema> defaultSchemas;
  private final Map<String, Schema> cachedSchemas;
  private final ObjectMapper objectMapper;
  private final String refPrefix;
  private final RefFactory refFactory;

  @Inject
  public SchemasCache(
    @DefaultSchemas Map<String, Schema> defaultSchemas,
    @CachedSchemas Map<String, Schema> cachedSchemas,
    @Internal ObjectMapper objectMapper,
    @RefPrefix String refPrefix,
    RefFactory refFactory
  ) {
    this.defaultSchemas = defaultSchemas;
    this.cachedSchemas = cachedSchemas;
    this.objectMapper = objectMapper;
    this.refPrefix = refPrefix;
    this.refFactory = refFactory;
  }

  public void clearCachedSchemas() {
    cachedSchemas.clear();
    cachedSchemas.putAll(defaultSchemas);
  }

  public Map<String, Schema> getAll() {
    return cachedSchemas;
  }

  public boolean contains(String typeName) {
    return cachedSchemas.containsKey(typeName);
  }

  public Schema resolve(Referenceable<Schema> refOrSchema) {
    if (!refOrSchema.isReferential()) {
      return Schema.class.cast(refOrSchema);
    }
    String referencedType =
      ((Ref<Schema>) refOrSchema).getRef().substring(refPrefix.length());
    Preconditions.checkArgument(
      cachedSchemas.containsKey(referencedType),
      "Schema " + referencedType + " not found"
    );
    return cachedSchemas.get(referencedType);
  }

  public Referenceable<Schema> putSchema(String name, Schema schema) {
    cachedSchemas.put(name, schema);
    return refFactory.create(name);
  }

  public Map<String, Schema> getReferenced(Referenceable<Schema> schema)
    throws SchemaGenerationException {
    Schema resolvedSchema = resolve(schema);

    Map<String, Schema> processed = Maps.newHashMap();
    Queue<String> processingQueue = Queues.newLinkedBlockingDeque(
      getDirectReferences(toJson(resolvedSchema))
    );
    do {
      String schemaName = processingQueue.poll();
      if (processed.containsKey(schemaName)) {
        continue;
      }
      processed.put(schemaName, cachedSchemas.get(schemaName));
      processingQueue.addAll(getDirectReferences(toJson(cachedSchemas.get(schemaName))));
    } while (!processingQueue.isEmpty());
    return ImmutableMap.copyOf(processed);
  }

  private String toJson(Schema schema) throws SchemaGenerationException {
    try {
      return objectMapper.writeValueAsString(schema);
    } catch (JsonProcessingException e) {
      throw new SchemaGenerationException("Unable to ", e);
    }
  }

  private Set<String> getDirectReferences(String schemaJson) {
    ImmutableSet.Builder<String> referenced = ImmutableSet.builder();
    Matcher matcher = REF_PATTERN.matcher(schemaJson);
    while (matcher.find()) {
      referenced.add(matcher.group("name"));
    }
    return referenced.build();
  }
}
