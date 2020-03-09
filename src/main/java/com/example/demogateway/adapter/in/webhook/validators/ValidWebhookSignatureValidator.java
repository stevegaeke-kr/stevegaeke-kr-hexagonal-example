package com.example.demogateway.adapter.in.webhook.validators;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValidWebhookSignatureValidator implements
    ConstraintValidator<ValidWebhookSignature, HttpServletRequest> {

  private static final String SIGNATURE_HEADER = "X-OSP-WEBHOOK-SIGNATURE";
  private static final String SIGNATURE_KEY_HEADER = "X-OSP-WEBHOOK-SIGNATURE-KEY";

  private static final String DEFAULT_SIGNATURE_VALUE
      = "H9FcRW0isRRMPgHZaa/+Vb4ueSnAKiKoxYtiaPVgQoKAw==";
  private static final String DEFAULT_SIGNATURE_KEY_VALUE
      = "9f4d72ee-18f2-469d-8a5e-a6b9fa8aa2cc";

  @Value("${webhook-validator.signature:" + DEFAULT_SIGNATURE_VALUE + "}")
  private String expectedSignature;

  @Value("${webhook-validator.signatureKey:" + DEFAULT_SIGNATURE_KEY_VALUE + "}")
  private String expectedSignatureKey;

  @Override
  public boolean isValid(
      final HttpServletRequest request,
      final ConstraintValidatorContext context
  ) {
    // Don't let exceptions escape validation checks; just return false.
    try {
      val actualSignatureOrNull = request.getHeader(SIGNATURE_HEADER);
      val actualSignatureKeyOrNull = request.getHeader(SIGNATURE_KEY_HEADER);

      return expectedSignature.equalsIgnoreCase(actualSignatureOrNull)
          && expectedSignatureKey.equalsIgnoreCase(actualSignatureKeyOrNull);
    } catch (final Exception ignore) {
      // Validation errors return false to indicate problems
    }
    return false;
  }
}
