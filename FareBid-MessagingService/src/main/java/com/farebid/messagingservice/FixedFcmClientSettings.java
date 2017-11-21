package com.farebid.messagingservice;

import de.bytefish.fcmjava.http.options.IFcmClientSettings;

public class FixedFcmClientSettings implements IFcmClientSettings {

  private final String apiKey;
  
  public FixedFcmClientSettings(String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  public String getApiKey() {
    return apiKey;
  }

  @Override
  public String getFcmUrl() {
    // TODO
    return "test";
  }

}
