package com.example.demogateway.adapter.out.ospclient;

import com.example.demogateway.application.port.out.PurchaseOrderRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
class OspClient implements PurchaseOrderRepository {

  private final OspClientProperties ospClientProperties;

  @Override
  public Optional<PurchaseOrderData> get(final String purchaseOrderId) {
    // TODO
    return Optional.empty();
  }

  @Override
  public void save(PurchaseOrderData purchaseOrderData) {
    // TODO
  }

  @Override
  public void cancel(final String purchaseOrderId) {
    // TODO
  }

  @Override
  public void reinstate(final String purchaseOrderId) {
    // TODO
  }
}
