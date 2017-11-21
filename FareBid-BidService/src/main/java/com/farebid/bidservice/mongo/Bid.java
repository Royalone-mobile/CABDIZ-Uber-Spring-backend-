package com.farebid.bidservice.mongo;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class Bid {

  @Id
  public String id;
  
  private String driverEmail;
  private Double price;
  private Date dateCreated = new Date();
  
  public Bid() {
    //
  }
  
  public Bid(String driverEmail, Double price) {
    super();
    this.driverEmail = driverEmail;
    this.price = price;
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
  public Double getPrice() {
    return price;
  }
  public void setPrice(Double price) {
    this.price = price;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }
}
