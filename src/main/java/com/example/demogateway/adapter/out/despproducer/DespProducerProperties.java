package com.example.demogateway.adapter.out.despproducer;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("desp-producer")
@ConstructorBinding
@Data
public class DespProducerProperties {

  @NotEmpty(message = "desp-producer.broker-address is required.")
  final String brokerAddress;
}
