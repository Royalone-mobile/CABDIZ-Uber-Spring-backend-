package com.farebid.bidservice.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import com.farebid.bidservice.controller.Driver;

public class BidRequest {
  
  @Id
  public String id;
  
  String customerEmail; 
  Double x;
  Double y;
  Double targetX;
  Double targetY;
  String bidId;
  Double defaultFee;
  Date dateCreated = new Date();
  List<Driver> drivers = new ArrayList<>();
  Driver winner;
  Boolean shared;
  
  public BidRequest() {
    //
  }
  
  public BidRequest(String customerEmail, Double x, Double y, Double targetX,
      Double targetY, String bidId, Double defaultFee, Boolean shared) {
    this.customerEmail = customerEmail;
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
    this.bidId = bidId;
    this.defaultFee = defaultFee;
    this.shared = shared;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getCustomerEmail() {
    return customerEmail;
  }
  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
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
  public Double getTargetX() {
    return targetX;
  }
  public void setTargetX(Double targetX) {
    this.targetX = targetX;
  }
  public Double getTargetY() {
    return targetY;
  }
  public void setTargetY(Double targetY) {
    this.targetY = targetY;
  }
  public String getBidId() {
    return bidId;
  }
  public void setBidId(String bidId) {
    this.bidId = bidId;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
  public List<Driver> getDrivers() {
    return drivers;
  }
  public void setDrivers(List<Driver> drivers) {
    this.drivers = drivers;
  }
  public Double getDefaultFee() {
    return defaultFee;
  }
  public void setDefaultFee(Double defaultFee) {
    this.defaultFee = defaultFee;
  }
  public Driver getWinner() {
    return winner;
  }
  public void setWinner(Driver winner) {
    this.winner = winner;
  }  
}
