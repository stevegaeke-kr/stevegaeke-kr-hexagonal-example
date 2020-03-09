package com.example.demogateway;

import com.example.demogateway.adapter.in.web.WebConfiguration;
import com.example.demogateway.adapter.in.webhook.WebhookConfiguration;
import com.example.demogateway.adapter.out.despproducer.DespProducerConfiguration;
import com.example.demogateway.adapter.out.ospclient.OspClientConfiguration;
import com.example.demogateway.application.ApplicationConfiguration;
import com.example.demogateway.common.JacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@SpringBootApplication  // <-- need to discuss explicit module imports versus package scan
@SpringBootConfiguration
@EnableAutoConfiguration
@Import({
    JacksonConfiguration.class,
    WebConfiguration.class,
    WebhookConfiguration.class,
    DespProducerConfiguration.class,
    OspClientConfiguration.class,
    ApplicationConfiguration.class})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
