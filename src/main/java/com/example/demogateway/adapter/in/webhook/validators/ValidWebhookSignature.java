package com.example.demogateway.adapter.in.webhook.validators;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidWebhookSignatureValidator.class})
public @interface ValidWebhookSignature {

  String message() default "Webhook signature is invalid.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
