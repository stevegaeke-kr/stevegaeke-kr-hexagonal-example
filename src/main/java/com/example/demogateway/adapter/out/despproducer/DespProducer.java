package com.example.demogateway.adapter.out.despproducer;

import com.example.demogateway.application.port.out.DomainEventDispatcher;
import org.springframework.stereotype.Component;

@Component
class DespProducer implements DomainEventDispatcher {

  @Override
  public <T> void dispatch(T event) {

  }
}
