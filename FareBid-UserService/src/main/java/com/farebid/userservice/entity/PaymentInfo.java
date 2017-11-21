package com.farebid.userservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PaymentInfo {

  private @Id @GeneratedValue Long id;
  private Long userId;
  private String email;
  private String cardNumber;
  private String cardExpiry;
  private String cardCvc;
  private String stripeId;
  
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public String getCardNumber() {
    return cardNumber;
  }
  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }
  public String getCardExpiry() {
    return cardExpiry;
  }
  public void setCardExpiry(String cardExpiry) {
    this.cardExpiry = cardExpiry;
  }
  public String getCardCvc() {
    return cardCvc;
  }
  public void setCardCvc(String cardCvc) {
    this.cardCvc = cardCvc;
  }
  public String getStripeId() {
    return stripeId;
  }
  public void setStripeId(String stripeId) {
    this.stripeId = stripeId;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
}
