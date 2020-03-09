package com.example.demogateway.adapter.in.webhook.validators;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ValidWebhookEventValidator implements
    ConstraintValidator<ValidWebhookEvent, HttpServletRequest> {

  private static final String EVENT_NAME_HEADER = "X-OSP-WEBHOOK-EVENT-NAME";
  private static final String EVENT_NAME_VERSION = "X-OSP-WEBHOOK-EVENT-VERSION";

  private String expectedEventName;
  private String expectedEventVersion;

  @Override
  public void initialize(final ValidWebhookEvent constraintAnnotation) {

    Assert.notNull(constraintAnnotation,
        "ValidEventWebhook initialize is missing contraint annotation.");
    Assert.hasText(constraintAnnotation.eventName(),
        "ValidEventWebhook initialize is missing eventName parameter.");
    Assert.hasText(constraintAnnotation.eventVersion(),
        "ValidEventWebhook initialize is missing eventVersion parameter.");

    expectedEventName = constraintAnnotation.eventName();
    expectedEventVersion = constraintAnnotation.eventVersion();
  }

  @Override
  public boolean isValid(
      final HttpServletRequest request,
      final ConstraintValidatorContext context
  ) {
    // Don't let exceptions escape validation checks; just return false.
    try {
      val actualEventNameOrNull = request.getHeader(EVENT_NAME_HEADER);
      val actualEventVersionOrNull = request.getHeader(EVENT_NAME_VERSION);

      return expectedEventName.equalsIgnoreCase(actualEventNameOrNull)
          && expectedEventVersion.equalsIgnoreCase(actualEventVersionOrNull);
    } catch (final Exception ignore) {
      // Validation errors return false to indicate problems
    }
    return false;
  }
}
