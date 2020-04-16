package io.harness.multiservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class App extends Application<AppConfiguration> {

  private static String SERVICE;
  private static String VERSION;
  private static String APP_ENV;
  private static String API_URL;
  private static String NEXT_SERVICE;

  public static String getService() { return SERVICE; }
  public static String getVersion() { return VERSION; }
  public static String getAppEnv() { return APP_ENV; }
  public static String getApiUrl() { return API_URL; }
  public static String getNextService() { return NEXT_SERVICE; }

  @Override
  public void run(AppConfiguration c, Environment e) {
    SERVICE = c.getService();
    VERSION = c.getVersion();
    NEXT_SERVICE = c.getNextService();
    APP_ENV = System.getenv("APP_ENV");
    API_URL = System.getenv("API_URL");

    e.jersey().register(new PayloadResource());
  }

  public static void main(String[] args) throws Exception {
    new App().run(args);
  }
}
