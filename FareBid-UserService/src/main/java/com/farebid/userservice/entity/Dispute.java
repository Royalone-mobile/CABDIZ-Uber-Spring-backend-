package com.farebid.userservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Dispute {

  private @Id @GeneratedValue Long id;
  private String email;
  private String tripId;
  private String bidId;
  
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getTripId() {
    return tripId;
  }
  public void setTripId(String tripId) {
    this.tripId = tripId;
  }
  public String getBidId() {
    return bidId;
  }
  public void setBidId(String bidId) {
    this.bidId = bidId;
  }
}
