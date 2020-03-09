package com.example.demogateway.common;


import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.util.Assert;

/**
 * Utility to help handle logging metadata
 */
public final class LoggerUtils {

  private static final String REQUEST_URI = "requestUri";
  private static final String REQUEST_HEADER_PREFIX = "requestHeader_";

  private static final Set<String> excludedHeaders = Set.of(
      "proxy-authorization",
      "proxy-authenticate",
      "authorization"
  );

  private LoggerUtils() {
  }

  /**
   * Execute a 'function' containing a logging statement within a provided metadata context. (Since
   * this is currently based on slf4j, the metadata context is handled via {@link MDC}; any previous
   * MDC context is restored after execution).
   *
   * @param logger   lambda / function to execute within the metadata context
   * @param metadata Metadata map
   */
  public static void logWithContext(
      final Runnable logger,
      final Map<String, String> metadata
  ) {

    Assert.notNull(logger, "logger is required");

    if (metadata == null) {
      logger.run();
    } else {
      final Map<String, String> previousContext = MDC.getCopyOfContextMap();
      try {
        metadata.forEach(MDC::put);
        logger.run();
      } finally {
        if (previousContext != null) {
          MDC.setContextMap(previousContext);
        } else {
          MDC.clear();
        }
      }
    }
  }

  public static void logWithContext(
      final Runnable logger,
      final HttpServletRequest request
  ) {
    final Map<String, String> metadata = makeMetadata(request);
    logWithContext(logger, metadata);
  }

  public static void logWithContext(
      final Runnable logger,
      final Map<String, String> metadata,
      final HttpServletRequest request
  ) {
    final Map<String, String> combinedMetadata = makeMetadata(request);
    combinedMetadata.putAll(metadata);
    logWithContext(logger, combinedMetadata);
  }

  @SuppressWarnings("unchecked")
  // Needed for the request.getAttribute(...) cast to Map<String, String>
  private static Map<String, String> makeMetadata(final HttpServletRequest request) {

    final HashMap<String, String> metadata = new HashMap<>(
        Map.of(REQUEST_URI, request.getRequestURI())
    );

    // Add headers
    metadata.putAll(Collections.list(request.getHeaderNames()).stream()
        .filter(n -> !excludedHeaders.contains(n))
        .collect(Collectors.toMap(n -> REQUEST_HEADER_PREFIX + n, request::getHeader))
    );

    // Add URI template variables defined by controllers
    if (request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE) instanceof Map) {
      metadata.putAll((Map<String, String>) request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE));
    }

    // Add query parameters
    metadata.putAll(request.getParameterMap().entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())))
    );

    return metadata;
  }
}
