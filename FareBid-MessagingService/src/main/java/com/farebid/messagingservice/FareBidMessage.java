package com.farebid.messagingservice;

import org.springframework.data.annotation.Id;

public class FareBidMessage {

  @Id
  public String id;
  
  private String token;
  private String email;
  private String text;
  private Boolean successfull;
  private String error;
  
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Boolean getSuccessfull() {
    return successfull;
  }
  public void setSuccessfull(Boolean successfull) {
    this.successfull = successfull;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }
}
