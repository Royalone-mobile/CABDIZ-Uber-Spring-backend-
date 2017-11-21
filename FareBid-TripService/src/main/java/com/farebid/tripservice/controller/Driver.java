package com.farebid.tripservice.controller;

public class Driver {

  private String name;
  private Double distance;
  private Double fee;
  private boolean accepted = false;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Double getDistance() {
    return distance;
  }
  public void setDistance(Double distance) {
    this.distance = distance;
  }
  public Double getFee() {
    return fee;
  }
  public void setFee(Double fee) {
    this.fee = fee;
  }
  public boolean isAccepted() {
    return accepted;
  }
  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }
}
