package io.harness.multiservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class Payload {
  private String service;
  private String track;
  private String version;
  private String appEnv;
  private String error;
}
