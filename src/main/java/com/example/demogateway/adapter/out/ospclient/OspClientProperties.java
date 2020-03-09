package com.example.demogateway.adapter.out.ospclient;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties("osp-client")
@ConstructorBinding
@Data
class OspClientProperties {

  @NotEmpty(message = "osp-client.base-url is required.")
  final String baseUrl;
}
