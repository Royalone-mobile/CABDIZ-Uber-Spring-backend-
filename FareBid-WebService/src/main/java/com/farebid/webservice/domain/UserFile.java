package com.farebid.webservice.domain;

import java.util.Date;

public class UserFile {
  
  private Long id;
  private Long userId;
  private String email;
  private String fileName;
  private UserFileType fileType;
  private Date dateCreated = new Date();
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public UserFileType getFileType() {
    return fileType;
  }
  public void setFileType(UserFileType fileType) {
    this.fileType = fileType;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }  
}
