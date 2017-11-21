package com.farebid.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.farebid.webservice.domain.FareBidApiResponse;
import com.farebid.webservice.domain.Role;
import com.farebid.webservice.domain.User;
import com.farebid.webservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value = "/user-service")
public class UserServiceController {

  @Autowired
  UserService userService;

  @RequestMapping(value = "")
  public String home() {
    return userService.getDefault();
  }
  
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public FareBidApiResponse getDetailedUser(String email){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    String userJson = userService.getDetailedUser(email);
    if(userJson != null){
      response.setSuccess(true);
      response.setData(userJson);
    }else{
      response.setSuccess(false);
      response.setErrorMessage("User Does Not Exist");
    }
    return response;
  }
  
  @RequestMapping(value = "/payment-info", method = RequestMethod.POST)
  public FareBidApiResponse getPaymentInfo(String email){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    String paymentInfoJson = userService.getPaymentInfoAsJson(email);
    if(paymentInfoJson != null){
      response.setSuccess(true);
      response.setData(paymentInfoJson);
    }else{
      response.setSuccess(false);
      response.setErrorMessage("User Does Not Exist");
    }
    return response;
  }

  @RequestMapping(value = "/check-email", method = RequestMethod.POST)
  public FareBidApiResponse checkEmail(String email){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    String existingUserJson = userService.getUserAsJson(email);
    if(existingUserJson != null){
      response.setSuccess(false);
      response.setErrorMessage("Already Exist");
    }else{
      response.setSuccess(true);
    }
    return response;
  }
  
  @RequestMapping(value = "/send-verification-code", method = RequestMethod.POST)
  public FareBidApiResponse sendVerificationCode(String email, String code){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    if(!StringUtils.hasText(code)){
      response.setSuccess(false);
      response.setErrorMessage("Code is Empty");
      return response;
    }
    boolean success = userService.sendVerificationCode(email, code);
    response.setSuccess(success);
    if(!success){
      response.setErrorMessage("Unable to Send Verification Code");
    }
    return response;
  }
  
  @RequestMapping(value = "/register-user", method = RequestMethod.POST)
  public FareBidApiResponse registerUser(String email, String first_name, String last_name, String phone_number, 
      String fileName, String password, String card_number, String card_expiry, String card_cvc, String postal_code, 
      String stripe_id){
    
    FareBidApiResponse response = new FareBidApiResponse();
    
    if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
      response.setSuccess(false);
      response.setErrorMessage("Email or Password can not be empty");
      return response;
    }
    
    if (!StringUtils.hasText(card_number) || !StringUtils.hasText(card_expiry) || !StringUtils.hasText(card_cvc) 
        || !StringUtils.hasText(stripe_id)) {
      response.setSuccess(false);
      response.setErrorMessage("Invalid credit card");
      return response;
    }
        
    String userJson = userService.registerUser(email, first_name, last_name, phone_number, fileName, password, card_number, 
        card_expiry, card_cvc, postal_code, stripe_id);
    if(userJson != null){
      response.setSuccess(true);
    }else{
      response.setSuccess(false);
      response.setErrorMessage("An Error Has Occured");
    }
    
    return response;
  }
  

  @RequestMapping(value = "/update-user", method = RequestMethod.POST)
  public FareBidApiResponse updateUser(String email, String first_name, String last_name, String phone_number){
    FareBidApiResponse response = new FareBidApiResponse();
    
    if (!StringUtils.hasText(email)) {
      response.setSuccess(false);
      response.setErrorMessage("Email or Password can not be empty");
      return response;
    }
    
    userService.updateUser(email, first_name, last_name, phone_number);
    response.setSuccess(true);
    return response;
  }
  
  @RequestMapping(value = "/update-payment-info", method = RequestMethod.POST)
  public FareBidApiResponse updatePaymentInfo(String email, String card_number, String card_expiry, 
      String card_cvc, String stripe_id) {
    FareBidApiResponse response = new FareBidApiResponse();
    
    if (!StringUtils.hasText(email)) {
      response.setSuccess(false);
      response.setErrorMessage("Email or Password can not be empty");
      return response;
    }
    
    userService.updatePaymentInfo(email, card_number, card_expiry, card_cvc, stripe_id);
    
    response.setSuccess(true);
    return response;
  }
  
  public User addUser(String email, String password, Role role, String firstname, String surename,
      String phone, String creditCard, String cvv, String expirationDate, String postalCode,
      String address, String city, String state, String vehicle, String vehicleCategory,
      String plate, String taxiCompany, Boolean babySeat, Boolean wheelChair)
      throws JsonProcessingException {
    return userService.addUser(email, password, role, firstname, surename, phone, creditCard, cvv,
        expirationDate, postalCode, address, city, state, vehicle, vehicleCategory, plate,
        taxiCompany, babySeat, wheelChair);
  }
  
  @RequestMapping(value = "/user/file", method = RequestMethod.POST)
  public FareBidApiResponse uploadFile(String fileName, String fileContent, String fileType){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(fileName)){
      response.setSuccess(false);
      response.setErrorMessage("File Name is Empty");
      return response;
    }
    if(!StringUtils.hasText(fileContent)){
      response.setSuccess(false);
      response.setErrorMessage("File Content is Empty");
      return response;
    }
    userService.addFile(fileName, fileContent, fileType);
    response.setSuccess(true);
    return response;
  }
  
  @RequestMapping(value = "/get-file", method = RequestMethod.POST)
  public FareBidApiResponse getFile(String email, String fileType){
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    if(!StringUtils.hasText(fileType)){
      response.setSuccess(false);
      response.setErrorMessage("File Type is Empty");
      return response;
    }
   
    String data = userService.getFile(email, fileType);
    response.setSuccess(true);
    response.setData(data);
    
    return response;
  }
  
  @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
  public FareBidApiResponse forgetPassword(String email) {
    FareBidApiResponse response = new FareBidApiResponse();
    if(!StringUtils.hasText(email)){
      response.setSuccess(false);
      response.setErrorMessage("Email is Empty");
      return response;
    }
    userService.forgetPassword(email);
    response.setSuccess(true);
    return response;
  }
}
