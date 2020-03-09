package com.example.demogateway.adapter.out.despproducer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@ComponentScan
@EnableKafka
@EnableConfigurationProperties(DespProducerProperties.class)
// If the module needs to be toggled, use a ConditionalOnProperty
@ConditionalOnProperty(name = "desp-producer.enabled", havingValue = "true", matchIfMissing = true)
public class DespProducerConfiguration {

}
