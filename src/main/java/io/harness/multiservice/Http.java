package io.harness.multiservice;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Http {

  public static Response
  getResponseFromUrl(String url, Map<String, String> headers,
                     int connectTimeoutSeconds, int readTimeoutSeconds)
      throws IOException {
    Request.Builder request = new Request.Builder().url(url);
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      request.addHeader(entry.getKey(), entry.getValue());
    }
    return new OkHttpClient.Builder()
        .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
        .readTimeout(readTimeoutSeconds, TimeUnit.SECONDS)
        .build()
        .newCall(request.build())
        .execute();
  }
}
