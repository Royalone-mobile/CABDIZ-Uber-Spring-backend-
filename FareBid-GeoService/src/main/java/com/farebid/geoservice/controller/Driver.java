package com.farebid.geoservice.controller;

public class Driver {

  private String name;
  private Double distance;
  
  public Driver(String name, Double distance) {
    super();
    this.name = name;
    this.distance = distance;
  }
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

}
