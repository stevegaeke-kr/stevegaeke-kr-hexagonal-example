package com.example.demogateway.common;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Jackson defaults for the entire application.
 */
@Configuration
public class JacksonConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()

        // Drop any null values from output
        .setSerializationInclusion(NON_NULL)

        // Can't use ISO8601 formatting without this module.
        // Use OffsetDateTime data type for time values to include timezone offsets.
        .registerModule(new JavaTimeModule())
        .disable(WRITE_DATES_AS_TIMESTAMPS)

        // API standards state errors should be reported for unknown POSTed fields
        .enable(FAIL_ON_UNKNOWN_PROPERTIES);

  }

}
