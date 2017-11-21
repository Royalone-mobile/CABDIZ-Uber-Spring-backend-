package com.farebid.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class UserServiceException extends Exception {

  public UserServiceException(String mes) {
    super(mes);
  }

  private static final long serialVersionUID = 6860833054159009645L;

}
