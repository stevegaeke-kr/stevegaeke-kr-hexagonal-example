package com.example.demogateway.application.port.out;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Value;

public interface PurchaseOrderRepository {

  Optional<PurchaseOrderData> get(final String id);

  void save(final PurchaseOrderData purchaseOrderData);

  void cancel(final String id);

  void reinstate(final String id);

  @Value
  class PurchaseOrderData {

    @Valid
    @NotEmpty(message = "purchaseOrderId cannot be missing.")
    private final String purchaseOrderId;

    @NotEmpty(message = "supplier cannot be empty.")
    private String supplier;
  }
}
