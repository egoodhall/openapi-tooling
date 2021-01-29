package com.github.emm035.openapi.annotation.processor.api;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface MethodMatcher {
  Set<? extends Class<? extends Annotation>> getAnnotations();
}
