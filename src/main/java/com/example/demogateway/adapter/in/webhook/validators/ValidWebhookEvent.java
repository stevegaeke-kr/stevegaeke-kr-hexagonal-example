package com.example.demogateway.adapter.in.webhook.validators;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

// X-OSP-WEBHOOK-EVENT-NAME: purchaseOrderPlaced
// X-OSP-WEBHOOK-EVENT-VERSION: 2

@Target(PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidWebhookEventValidator.class})
public @interface ValidWebhookEvent {

  String message() default "Webhook event name and version combination are invalid.";

  String eventName();

  String eventVersion();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
