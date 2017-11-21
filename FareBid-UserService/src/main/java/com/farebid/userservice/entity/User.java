package com.farebid.userservice.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users")
public class User {

  private @Id @GeneratedValue Long id;
  private String email;
  private String password;
  private String forename;
  private String surename;
  private String phone;
  private String postalCode;
  private @Enumerated(EnumType.STRING) Gender gender;
  private @Enumerated(EnumType.STRING) Role role;
  private Date dateCreated = new Date();
  private Date dateLastLogin;
  private Date dateBirthDay;
//  private String city;
//  private String addressDetail;
//  private String creditCard;
//  private String cvv;
//  private String expirationDate;
//  private String postalCode;
//  private String state;
//  private String vehicle;
//  private String vehicleCategory;
//  private String plate;
//  private String taxiCompany; 
//  private Boolean babySeat;
//  private Boolean wheelChair;

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

  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Date getDateLastLogin() {
    return dateLastLogin;
  }

  public void setDateLastLogin(Date dateLastLogin) {
    this.dateLastLogin = dateLastLogin;
  }

  public Date getDateBirthDay() {
    return dateBirthDay;
  }

  public void setDateBirthDay(Date dateBirthDay) {
    this.dateBirthDay = dateBirthDay;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
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
