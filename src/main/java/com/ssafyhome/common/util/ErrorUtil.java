package com.ssafyhome.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ErrorUtil {

  public Map<String, Object> getErrorDetails(HttpServletRequest request, Exception exception) {

    String methodName;
    String requiredAuthorities = "Unknown";

    try {
      HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
      Method method = handlerMethod.getMethod();
      methodName = method.getDeclaringClass().getSimpleName() + "#" + method.getName();

      PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
      if (preAuthorize != null) requiredAuthorities = preAuthorize.value();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentAuthorities = "No authorities";
    if (authentication != null) {
      currentAuthorities = authentication.getAuthorities().iterator().next().getAuthority();
    }

    return Map.of(
        "error", exception instanceof AccessDeniedException ? "해당 접근에 대한 권한이 필요합니다." : "인증이 필요합니다.",
        "message", exception.getMessage(),
        "method", methodName,
        "requiredAuthorities", requiredAuthorities,
        "currentAuthorities", currentAuthorities
    );
  }
}
