package com.example.demogateway.adapter.in.webhook;

import com.example.demogateway.adapter.in.webhook.validators.ValidWebhookEvent;
import com.example.demogateway.adapter.in.webhook.validators.ValidWebhookSignature;
import com.example.demogateway.application.port.in.PlacePurchaseOrderUseCase;
import com.example.demogateway.application.port.in.PlacePurchaseOrderUseCase.PurchaseOrderPlacedCommand;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
class PurchaseOrderWebhookController {

  private static final String PURCHASE_ORDER_PLACED_URI = "purchaseOrderPlaced";

  final PlacePurchaseOrderUseCase placePurchaseOrderUseCase;

  @PostMapping(PURCHASE_ORDER_PLACED_URI)
  void purchaseOrderPlaced(

      @ValidWebhookSignature()
      @ValidWebhookEvent(eventName = "purchaseOrderPlaced", eventVersion = "2")
      final HttpServletRequest request,

      @NotEmpty(message = "purchaseOrderId is missing")
      @RequestBody final String purchaseOrderId
  ) {

    // 1. Build a command
    val command = new PurchaseOrderPlacedCommand(
        UUID.randomUUID().toString(),
        1,
        purchaseOrderId);

    // 2. Call a use case
    placePurchaseOrderUseCase.placePurchaseOrder(command);

  }

  // follow pattern for updatePurchaseOrder, cancelPurchaseOrder and reinstatePurchaseOrder
}
