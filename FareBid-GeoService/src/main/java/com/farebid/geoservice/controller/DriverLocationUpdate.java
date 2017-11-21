package com.farebid.geoservice.controller;

public class DriverLocationUpdate {
  
  Double x;
  Double y;
  Boolean vacant;
  
  public DriverLocationUpdate() {

  }
    
  public DriverLocationUpdate(Double x, Double y, Boolean vacant) {
    super();
    this.x = x;
    this.y = y;
    this.vacant = vacant;
  }
  
  public Double getX() {
    return x;
  }
  public void setX(Double x) {
    this.x = x;
  }
  public Double getY() {
    return y;
  }
  public void setY(Double y) {
    this.y = y;
  }
  public Boolean getVacant() {
    return vacant;
  }
  public void setVacant(Boolean vacant) {
    this.vacant = vacant;
  }
}
