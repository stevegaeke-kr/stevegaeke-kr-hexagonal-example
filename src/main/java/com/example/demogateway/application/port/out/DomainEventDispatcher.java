package com.example.demogateway.application.port.out;

public interface DomainEventDispatcher {

  <T> void dispatch(final T event);

}
