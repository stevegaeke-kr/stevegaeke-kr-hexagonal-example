package com.example.demogateway.common;

import static org.springframework.util.StringUtils.hasText;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class StringUtils {

  private StringUtils() {
    /* no instances needed */
  }

  private static final PropertyNamingStrategy.SnakeCaseStrategy snakeCaseConverter =
      new PropertyNamingStrategy.SnakeCaseStrategy();

  public static String camelToSnake(final String camelCase) {

    return (hasText(camelCase))
        ? snakeCaseConverter.translate(camelCase)
            .toUpperCase()
            .replace(".", "_")
            .replace("EXCEPTION", "ERROR")
        : "UNKNOWN";
  }
}
