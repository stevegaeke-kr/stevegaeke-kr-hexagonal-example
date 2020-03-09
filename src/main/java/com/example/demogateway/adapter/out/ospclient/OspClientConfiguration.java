package com.example.demogateway.adapter.out.ospclient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableConfigurationProperties(OspClientProperties.class)
public class OspClientConfiguration {

}
