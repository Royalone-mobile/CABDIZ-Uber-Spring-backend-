package com.farebid.webservice.service;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.farebid.webservice.domain.Client;
import com.farebid.webservice.domain.Role;
import com.farebid.webservice.domain.User;
import com.farebid.webservice.domain.UserFile;
import com.farebid.webservice.domain.UserLoginInfoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class UserService {

  private final String userServiceUrl = "http://user-service";

  @Autowired
  @LoadBalanced
  RestTemplate restTemplate;

  public String getDefault() {
    ResponseEntity<String> response = restTemplate.getForEntity(userServiceUrl, String.class);
    return response.getBody();
  }

  public UserLoginInfoDTO getUser(String email) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<UserLoginInfoDTO> response =
          restTemplate.exchange(userServiceUrl + "/user/login?email=" + email, HttpMethod.GET,
              entity, UserLoginInfoDTO.class);
    return response.getBody();
  }
  
  public String getUserAsJson(String email) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<String> response =
          restTemplate.exchange(userServiceUrl + "/user/login?email=" + email, HttpMethod.GET,
              entity, String.class);
    return response.getBody();
  }
  
  public String getPaymentInfoAsJson(String email) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<String> response =
          restTemplate.exchange(userServiceUrl + "/user/get-payment-info?email=" + email, HttpMethod.GET,
              entity, String.class);
    return response.getBody();
  }
  
  public String getDetailedUser(String email){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<String> response =
          restTemplate.exchange(userServiceUrl + "/user?email=" + email, HttpMethod.GET,
              entity, String.class);
    return response.getBody();
  }
  
  public String registerUser(String email, String first_name, String last_name, String phone_number, String fileName,
      String password, String card_number, String card_expiry, String card_cvc, String postal_code, String stripe_id){
   
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(userServiceUrl + "/user/register-user?email=" + email + "&password=" + password
          + "&first_name=" + first_name + "&last_name=" + last_name + "&phone_number="
          + phone_number + "&card_number=" + card_number + "&card_expiry=" + card_expiry + "&card_cvc="
          + card_cvc + "&postal_code=" + postal_code + "&stripe_id=" + stripe_id + "&fileName=" + fileName);
      
    ResponseEntity<String> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, String.class);
    return response.getBody();

  }

  public String updatePaymentInfo(String email, String card_number, String card_expiry, String card_cvc, String stripe_id){
   
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(userServiceUrl + "/user/update-payment-info?email=" + email 
          + "&card_number=" + card_number + "&card_expiry=" + card_expiry + "&card_cvc="
          + card_cvc + "&stripe_id=" + stripe_id);
      
    ResponseEntity<String> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, String.class);
    return response.getBody();
  }
  
  public String updateUser(String email, String first_name, String last_name, String phone_number){
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(userServiceUrl + "/user/update-user?email=" + email 
          + "&first_name=" + first_name + "&last_name=" + last_name + "&phone_number="
          + phone_number);
      
    ResponseEntity<String> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, String.class);
    return response.getBody();
  }
  
  public User addUser(String email, String password, Role role, String firstname, String surename,
      String phone, String creditCard, String cvv, String expirationDate, String postalCode,
      String address, String city, String state, String vehicle, String vehicleCategory,
      String plate, String taxiCompany, Boolean babySeat, Boolean wheelChair)
      throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    try {
      StringBuffer request = new StringBuffer();
      request.append(userServiceUrl + "/user/add?email=" + email + "&password=" + password
          + "&role=" + role + "&firstname=" + firstname + "&surename=" + surename + "&phone="
          + phone + "&creditCard=" + creditCard + "&cvv=" + cvv + "&expirationDate="
          + expirationDate + "&postalCode=" + postalCode + "&address=" + address + "&city=" + city
          + "&state=" + state + "&vehicle=" + vehicle + "&vehicleCategory=" + vehicleCategory
          + "&plate=" + plate + "&taxiCompany=" + taxiCompany);
      if (babySeat != null) {
        request.append("&babySeat=" + babySeat);
      }
      if (wheelChair != null) {
        request.append("&wheelChair=" + wheelChair);
      }
      ResponseEntity<User> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, User.class);
      return response.getBody();
    } catch (Exception e) {
      return null;
    }
  }

  public Client getClient(String clientType) throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Client> response = restTemplate.exchange(
        userServiceUrl + "/client?clientType=" + clientType, HttpMethod.GET, entity, Client.class);
    return response.getBody();
  }

  public void addFile(String fileName, String fileContent, String fileType) {
    byte[] file = Base64.getMimeDecoder().decode(fileContent);
    Storage storage = StorageOptions.getDefaultInstance().getService();
    BlobId blobId = BlobId.of("test-farebid", fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
    System.err.println("before uplading fle");
    Blob blob = storage.create(blobInfo, file);
    System.err.println("after uploading file");

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    restTemplate.exchange(
        userServiceUrl + "/user/upload-file?fileName=" + fileName + "&fileType=" + fileType,
        HttpMethod.GET, entity, String.class);
  }
  
  public String getFile(String email, String fileType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<UserFile> response = restTemplate.exchange(
        userServiceUrl + "/user/get-file?email=" + email + "&fileType=" + fileType,
        HttpMethod.GET, entity, UserFile.class);
  
    
    if(response == null || response.getBody() == null){
      System.err.println("response is null, for email: " + email + " and fileType: " + fileType);
      return null;
    }
    
    Storage storage = StorageOptions.getDefaultInstance().getService();
    String fileName = response.getBody().getFileName();
    BlobId blobId = BlobId.of("test-farebid", fileName);
    System.err.println("before requesting fle");
    byte[] file = storage.readAllBytes(blobId);
    System.err.println("after uploading file");
    String result = Base64.getMimeEncoder().encodeToString(file);
    return result;
  }
  
  public void forgetPassword(String email) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);

    restTemplate.exchange(userServiceUrl + "/user/forgetPassword?email=" + email, HttpMethod.GET,
              entity, String.class);

  }
  
  public boolean sendVerificationCode(String email, String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    try {
      ResponseEntity<Boolean> response = restTemplate.exchange(userServiceUrl + 
          "/user/send-verification-code?email=" + email + "&code="+code, HttpMethod.GET,
              entity, Boolean.class);
      return response.getBody();
    } catch (Exception e) {
      return false;
    }
  }
}
