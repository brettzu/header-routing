package io.harness.multiservice;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application<AppConfiguration> {

  private static final int DEFAULT_PORT = 8080;

  private static String SERVICE;
  private static String VERSION;
  private static String APP_ENV;
  private static String NEXT_SERVICE;
  private static String NEXT_SERVICE_URL;

  public static String getService() { return SERVICE; }
  public static String getVersion() { return VERSION; }
  public static String getAppEnv() { return APP_ENV; }
  public static String getNextService() { return NEXT_SERVICE; }
  public static String getNextServiceUrl() { return NEXT_SERVICE_URL; }

  @Override
  public void run(AppConfiguration c, Environment e) {

    SERVICE = c.getService();
    VERSION = c.getVersion();
    NEXT_SERVICE = c.getNextService();
    APP_ENV = System.getenv("APP_ENV");

    String group = System.getenv("GROUP");
    String portString = System.getenv("PORT");
    int port = DEFAULT_PORT;
    try {
      port = parseInt(portString);
    } catch (NumberFormatException ex) {
      log.warn(format("Could not parse port [%s]. Using default [%d]",
                      portString, DEFAULT_PORT),
               ex);
    }

    NEXT_SERVICE_URL = format("http://%s-%s:%d/%s", group, getNextService(),
                              port, getNextService());

    e.jersey().register(new PayloadResource());
  }

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }
}
