package io.harness.multiservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Data;

@Data
public class AppConfiguration extends Configuration {
  @JsonProperty("service") private String service;
  @JsonProperty("version") private String version;
  @JsonProperty("nextService") private String nextService;
}
