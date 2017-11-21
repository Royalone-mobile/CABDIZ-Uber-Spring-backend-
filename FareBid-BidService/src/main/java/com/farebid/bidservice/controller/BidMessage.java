package com.farebid.bidservice.controller;

import java.util.Date;
import java.util.List;

public class BidMessage {

  private String id;
  private List<Driver> shortListedDrivers;
  private double targetX;
  private double targetY;
  private Double defaultFee;
  private Boolean finished = false;
  private Driver winner;
  Date tripStart;
    
  public List<Driver> getShortListedDrivers() {
    return shortListedDrivers;
  }
  public void setShortListedDrivers(List<Driver> shortListedDrivers) {
    this.shortListedDrivers = shortListedDrivers;
  }
  public Double getDefaultFee() {
    return defaultFee;
  }
  public void setDefaultFee(Double defaultFee) {
    this.defaultFee = defaultFee;
  }
  public double getTargetX() {
    return targetX;
  }
  public void setTargetX(double targetX) {
    this.targetX = targetX;
  }
  public double getTargetY() {
    return targetY;
  }
  public void setTargetY(double targetY) {
    this.targetY = targetY;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Boolean getFinished() {
    return finished;
  }
  public void setFinished(Boolean finished) {
    this.finished = finished;
  }
  public Driver getWinner() {
    return winner;
  }
  public void setWinner(Driver winner) {
    this.winner = winner;
  }
  public Date getTripStart() {
    return tripStart;
  }
  public void setTripStart(Date tripStart) {
    this.tripStart = tripStart;
  }
}
