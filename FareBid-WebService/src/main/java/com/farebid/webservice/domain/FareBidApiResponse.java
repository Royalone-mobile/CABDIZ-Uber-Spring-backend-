package com.farebid.webservice.domain;

public class FareBidApiResponse {

  private boolean success;
  private String errorMessage;
  private String data;
  
  public boolean isSuccess() {
    return success;
  }
  public void setSuccess(boolean success) {
    this.success = success;
  }
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }
  
}
