package com.github.emm035.openapi.annotation.processor.jaxrs;

import com.github.emm035.openapi.annotation.processor.api.parsers.PathParser;
import java.util.Optional;
import javax.lang.model.element.ExecutableElement;
import javax.ws.rs.Path;

public class JaxRsPathParser implements PathParser {

  @Override
  public boolean canParse(ExecutableElement element) {
    return true;
  }

  @Override
  public String parse(ExecutableElement element) {
    Optional<String> methodPath = Optional
      .ofNullable(element.getAnnotation(Path.class))
      .map(Path::value);
    Optional<String> classPath = Optional
      .ofNullable(element.getEnclosingElement().getAnnotation(Path.class))
      .map(Path::value);
    return joinPaths(classPath, methodPath);
  }

  private String joinPaths(Optional<String> classPath, Optional<String> methodPath) {
    if (classPath.isEmpty() && methodPath.isEmpty()) {
      return "/";
    }
    if (classPath.isEmpty()) {
      return methodPath.get().startsWith("/") ? methodPath.get() : "/" + methodPath.get();
    }
    if (methodPath.isEmpty()) {
      return classPath.get().startsWith("/") ? classPath.get() : "/" + classPath.get();
    }
    return (
      (classPath.get().startsWith("/") ? classPath.get() : "/" + classPath.get()) +
      (
        (methodPath.get().startsWith("/") || classPath.get().endsWith("/"))
          ? methodPath.get()
          : "/" + methodPath.get()
      )
    );
  }
}
