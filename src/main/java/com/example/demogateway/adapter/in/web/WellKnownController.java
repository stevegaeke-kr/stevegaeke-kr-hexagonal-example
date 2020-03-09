package com.example.demogateway.adapter.in.web;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping(WellKnownController.BASE_RESOURCE_URL)
public class WellKnownController {

  // TODO: Change 'resources' to the plural form of the resource served
  static final String BASE_RESOURCE_URL = "/v1/resources";

  private final HealthEndpoint healthEndpoint;

  @GetMapping(value = "/.well-known/openapi.json", produces = APPLICATION_JSON_VALUE)
  public String getOpenApiJson() throws IOException {
    val jsonMapper = new ObjectMapper();
    val ymlMapper = new ObjectMapper(new YAMLFactory());
    val ymlStream = new ClassPathResource("openapi-v1.yml").getInputStream();
    return jsonMapper.writeValueAsString(ymlMapper.readValue(ymlStream, Object.class));
  }

  // Must respond within 10 msec (ref: http://guides.pages.kroger.com/api/best-practices-v3/health/)
  @GetMapping("/.well-known/heartbeat")
  // TODO: Find replacement for deprecated ApplicationHealthIndicator class.
  public Status heartbeat() {
    return new ApplicationHealthIndicator().health().getStatus();
  }

  // Must respond within 100 msec (ref: http://guides.pages.kroger.com/api/best-practices-v3/health/)
  // Note that Spring health information is cached for 1s by default
  @GetMapping("/.well-known/health-check")
  public ResponseEntity<?> healthCheck() {
    val health = healthEndpoint.health();

    return health.getStatus().equals(Status.UP)
        ? ResponseEntity.ok(health)
        : ResponseEntity.status(SERVICE_UNAVAILABLE).body(health);
  }
}
