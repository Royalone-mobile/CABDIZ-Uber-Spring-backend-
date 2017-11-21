package com.farebid.webservice.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TripService {

  private final String tripServiceUrl = "http://trip-service";

  @Autowired
  @LoadBalanced
  RestTemplate restTemplate;
  
  public Double getEstimatedFee(Double distance){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(tripServiceUrl + "/trip/estimated-fee?distance=" + distance);
      
    ResponseEntity<Double> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, Double.class);
    return response.getBody();
  }
  
  public String getTripHistoryForCustomer(String customerEmail){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(tripServiceUrl + "/trip/history/customer?customerEmail=" + customerEmail);
      
    ResponseEntity<String> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, String.class);
    return response.getBody();
  }
  
  public String getTripHistoryForDriver(String driverEmail){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(headers);
    StringBuffer request = new StringBuffer();
    request.append(tripServiceUrl + "/trip/history/customer?driverEmail=" + driverEmail);
      
    ResponseEntity<String> response =
          restTemplate.exchange(request.toString(), HttpMethod.GET, entity, String.class);
    return response.getBody();
  }
}
