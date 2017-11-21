package com.farebid.bidservice.mongo;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class BookingCancellation {

  @Id
  public String id;
  
  public String bookingId;
  public String driverEmail;
  public Date dateCreated = new Date();
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getBookingId() {
    return bookingId;
  }
  public void setBookingId(String bookingId) {
    this.bookingId = bookingId;
  }
  public String getDriverEmail() {
    return driverEmail;
  }
  public void setDriverEmail(String driverEmail) {
    this.driverEmail = driverEmail;
  }
}
