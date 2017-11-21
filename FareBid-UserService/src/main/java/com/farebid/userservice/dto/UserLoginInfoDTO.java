package com.farebid.userservice.dto;

import com.farebid.userservice.entity.User;

public class UserLoginInfoDTO {
  
  private String email;
  private String password;
  private String role;
  
  public UserLoginInfoDTO(){
    //
  }
  
  public UserLoginInfoDTO(User user) {
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.role = user.getRole().toString();
  }
  
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role;
  }
}
