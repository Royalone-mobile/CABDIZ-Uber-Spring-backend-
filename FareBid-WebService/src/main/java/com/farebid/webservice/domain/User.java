package com.farebid.webservice.domain;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class User {

  private Long id;
  private String email;
  private String password;
  private String forename;
  private String surename;
  private String phone;
  private Gender gender;
  private Role role;
  private LocalDateTime dateCreated = LocalDateTime.now();
  private LocalDateTime dateLastLogin;
  private LocalDateTime dateBirthDay;
  private String city;
  private String addressDetail;
  private String creditCard;
  private String cvv;
  private String expirationDate;
  private String postalCode;
  private String state;
  private String vehicle;
  private String vehicleCategory;
  private String plate;
  private String taxiCompany; 
  private Boolean babySeat;
  private Boolean wheelChair;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = new BCryptPasswordEncoder().encode(password);
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(String forename) {
    this.forename = forename;
  }

  public String getSurename() {
    return surename;
  }

  public void setSurename(String surename) {
    this.surename = surename;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDateTime getDateLastLogin() {
    return dateLastLogin;
  }

  public void setDateLastLogin(LocalDateTime dateLastLogin) {
    this.dateLastLogin = dateLastLogin;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAddressDetail() {
    return addressDetail;
  }

  public void setAddressDetail(String addressDetail) {
    this.addressDetail = addressDetail;
  }

  public LocalDateTime getDateBirthDay() {
    return dateBirthDay;
  }

  public void setDateBirthDay(LocalDateTime dateBirthDay) {
    this.dateBirthDay = dateBirthDay;
  }
  
  public String getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(String creditCard) {
    this.creditCard = creditCard;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  
  public String getVehicle() {
    return vehicle;
  }

  public void setVehicle(String vehicle) {
    this.vehicle = vehicle;
  }

  public String getVehicleCategory() {
    return vehicleCategory;
  }

  public void setVehicleCategory(String vehicleCategory) {
    this.vehicleCategory = vehicleCategory;
  }

  public String getPlate() {
    return plate;
  }

  public void setPlate(String plate) {
    this.plate = plate;
  }

  public String getTaxiCompany() {
    return taxiCompany;
  }

  public void setTaxiCompany(String taxiCompany) {
    this.taxiCompany = taxiCompany;
  }

  public Boolean getBabySeat() {
    return babySeat;
  }

  public void setBabySeat(Boolean babySeat) {
    this.babySeat = babySeat;
  }

  public Boolean getWheelChair() {
    return wheelChair;
  }

  public void setWheelChair(Boolean wheelChair) {
    this.wheelChair = wheelChair;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  } 
}
