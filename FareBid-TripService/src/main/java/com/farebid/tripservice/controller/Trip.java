package com.farebid.tripservice.controller;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Trip {

  @Id
  public String id;
  
  private String driverEmail;
  private String customerEmail;
  private Double initialPrice;
  private Double finalPrice;
  private Double x;
  private Double y;
  private Double targetX;
  private Double targetY;
  private Double finalX;
  private Double finalY;
  private boolean started;
  private boolean finished;
  private Date dateCreated = new Date();
  private Date dateFinished;
  private Date dateUpdated;
  
  public Trip() {
    //
  }
  
  
  public Trip(String driverEmail, String customerEmail, Double initialPrice, Double x, Double y, Double targetX,
      Double targetY) {
    super();
    this.driverEmail = driverEmail;
    this.customerEmail = customerEmail;
    this.initialPrice = initialPrice;
    this.x = x;
    this.y = y;
    this.targetX = targetX;
    this.targetY = targetY;
    this.started = true;
  }


  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getDriverEmail() {
    return driverEmail;
  }
  public void setDriverEmail(String driverEmail) {
    this.driverEmail = driverEmail;
  }
  public Double getInitialPrice() {
    return initialPrice;
  }
  public void setInitialPrice(Double initialPrice) {
    this.initialPrice = initialPrice;
  }
  public Double getFinalPrice() {
    return finalPrice;
  }
  public void setFinalPrice(Double finalPrice) {
    this.finalPrice = finalPrice;
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
  public Double getFinalX() {
    return finalX;
  }
  public void setFinalX(Double finalX) {
    this.finalX = finalX;
  }
  public Double getFinalY() {
    return finalY;
  }
  public void setFinalY(Double finalY) {
    this.finalY = finalY;
  }
  public boolean isStarted() {
    return started;
  }
  public void setStarted(boolean started) {
    this.started = started;
  }
  public boolean isFinished() {
    return finished;
  }
  public void setFinished(boolean finished) {
    this.finished = finished;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
  public Date getDateFinished() {
    return dateFinished;
  }
  public void setDateFinished(Date dateFinished) {
    this.dateFinished = dateFinished;
  }
  public Date getDateUpdated() {
    return dateUpdated;
  }
  public void setDateUpdated(Date dateUpdated) {
    this.dateUpdated = dateUpdated;
  }
  public String getCustomerEmail() {
    return customerEmail;
  }
  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }
}
