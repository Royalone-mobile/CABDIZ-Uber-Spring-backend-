package com.farebid.bidservice.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.farebid.bidservice.controller.Driver;

public class Booking {
  
  @Id
  public String id;
  
  String customerEmail;
  Double x; 
  Double y; 
  Double targetX;
  Double targetY; 
  Date tripStart;
  Boolean finalized = false;
  Boolean biddingStarted = false;
  Date dateCreated = new Date();
  List<Driver> drivers = new ArrayList<>();
  Driver winner;
  Date dateCancelled;
  Boolean shared;
  
  public Booking(){
    //
  }
  
  public Booking(String customerEmail, Double x, Double y, Double targetX, Double targetY, Date tripStart, Boolean shared) {
    super();
    this.customerEmail = customerEmail;
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
    this.tripStart = tripStart;
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
  public Date getTripStart() {
    return tripStart;
  }
  public void setTripStart(Date tripStart) {
    this.tripStart = tripStart;
  }
  public Boolean getFinalized() {
    return finalized;
  }
  public void setFinalized(Boolean finalized) {
    this.finalized = finalized;
  }
  public Boolean getBiddingStarted() {
    return biddingStarted;
  }
  public void setBiddingStarted(Boolean biddingStarted) {
    this.biddingStarted = biddingStarted;
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
  public Driver getWinner() {
    return winner;
  }
  public void setWinner(Driver winner) {
    this.winner = winner;
  }
  public Date getDateCancelled() {
    return dateCancelled;
  }
  public void setDateCancelled(Date dateCancelled) {
    this.dateCancelled = dateCancelled;
  }

  public Boolean getShared() {
    return shared;
  }

  public void setShared(Boolean shared) {
    this.shared = shared;
  }
}
