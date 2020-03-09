package com.example.demogateway.application.service;

import com.example.demogateway.application.port.in.PlacePurchaseOrderUseCase;
import com.example.demogateway.application.port.out.DomainEventDispatcher;
import com.example.demogateway.application.port.out.PurchaseOrderRepository;
import local.test.desp.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class PlacePlacePurchaseOrderService implements PlacePurchaseOrderUseCase {

  private final PurchaseOrderRepository purchaseOrderRepository;
  private final DomainEventDispatcher domainEventDispatcher;
  private final PurchaseOrderMapper purchaseOrderMapper;

  @Override
  public void placePurchaseOrder(final PurchaseOrderPlacedCommand command) {

    val ocadoPurchaseOrder = purchaseOrderRepository.get(command.getPurchaseOrderId());

    // TODO: map purchaseOrder to corresponding DESP event
    // ...

    val despPurchaseOrder = PurchaseOrder.newBuilder()
        .setPurchaseOrderId("1234")
        .setDesciption("My test purchase order")
        .build();

    domainEventDispatcher.dispatch(despPurchaseOrder);
  }
}
