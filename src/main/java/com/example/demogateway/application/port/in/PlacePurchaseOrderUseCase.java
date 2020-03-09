package com.example.demogateway.application.port.in;

import com.example.demogateway.common.SelfValidating;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;

public interface PlacePurchaseOrderUseCase {

  void placePurchaseOrder(final PurchaseOrderPlacedCommand command);

  // TODO: Decide if use cases are driven by primitive parameters or a single command object.
  @Getter
  class PurchaseOrderPlacedCommand extends SelfValidating<PurchaseOrderPlacedCommand> {

    @NotEmpty(message = "correlationId is required.")
    private final String correlationId;

    @NotEmpty(message = "purchaseOrderId is required.")
    private final String purchaseOrderId;

    @Min(value = 1, message = "attempt should be greater than 0.")
    int attempt;

    private final LocalDateTime timestamp = LocalDateTime.now();

    public PurchaseOrderPlacedCommand(
        @NotEmpty(message = "correlationId is required.") String correlationId,
        @Min(value = 1, message = "attempt should be greater than 0.") int attempt,
        @NotEmpty(message = "purchaseOrderId is required.") String purchaseOrderId) {

      this.correlationId = correlationId;
      this.attempt = attempt;
      this.purchaseOrderId = purchaseOrderId;

      this.validateSelf();
    }
  }
}
