package io.harness.multiservice;

import static io.harness.multiservice.App.getAppEnv;
import static io.harness.multiservice.App.getNextService;
import static io.harness.multiservice.App.getNextServiceUrl;
import static io.harness.multiservice.App.getService;
import static io.harness.multiservice.App.getVersion;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.google.common.collect.ImmutableList;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PayloadService {
  private static final String DEFAULT_TRACK = "default";

  private static final ObjectMapper mapper = new ObjectMapper();

  public List<Payload> getPayloads(String track) {
    List<Payload> payloads = new ArrayList<>();
    payloads.add(getPayload(track));
    if (isNotBlank(getNextService())) {
      payloads.addAll(getRemotePayloads(track));
    }
    return payloads;
  }

  private Payload getPayload(String track) {
    return Payload.builder()
        .service(getService())
        .track(isBlank(track) ? DEFAULT_TRACK : track)
        .version(getVersion())
        .appEnv(getAppEnv())
        .build();
  }

  private List<Payload> getRemotePayloads(String track) {
    String url = getNextServiceUrl();
    Map<String, String> headers = new HashMap<>();
    if (isNotBlank(track)) {
      headers.put("track", track);
    }
    try {
      Response response = Http.getResponseFromUrl(url, headers, 10, 10);
      if (response.code() != 200) {
        String msg =
            format("Error for url [%s] with track [%s]. Response %s - %s", url,
                   track, response.code(), response.message());
        log.error(msg);
        return ImmutableList.of(Payload.builder().error(msg).build());
      }
      ResponseBody responseBody = response.body();
      if (responseBody != null) {
        String responseString = responseBody.string();
        if (isNotBlank(responseString)) {
          List<Payload> payloads =
              (List<Payload>)mapper.readValue(responseString, List.class);
          return payloads;
        }
      }

    } catch (Exception e) {
      log.error("Failed to get remote payloads.", e);
    }
    return new ArrayList<>();
  }
}
