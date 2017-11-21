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

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class BidService {

  private final String bidServiceUrl = "http://bid-service";
  
  @Autowired
  @LoadBalanced   
  RestTemplate restTemplate;
  
  public String initiateBid(String customerEmail, Double x, Double y, Double targetX, Double targetY, 
      Double distance, Double defaultFee){
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(bidServiceUrl+"/bid/initiate?x="+x+"&y="+y
        +"&targetX="+targetX+"&targetY="+targetY+"&customerEmail=" + customerEmail+"&distance=" + distance
        +"&defaultFee=" + defaultFee, HttpMethod.GET, entity, String.class);
    return response.getBody();
  }
  
  public void bid(String customerEmail, Double amount, String bidId) throws JsonProcessingException{
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    
    restTemplate.exchange(bidServiceUrl+"/bid/"+bidId+"?customerEmail="+customerEmail +
        "&amount="+amount, HttpMethod.GET, entity, String.class);
  }
  
  public String booking(String customerEmail, Double x, Double y, Double targetX, Double targetY, String tripStart){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(bidServiceUrl+"/bid/booking?customerEmail="+customerEmail +
        "&x="+x+"&y="+y+"&targetX="+targetX+"&targetY="+targetY+"&tripStart="+tripStart, HttpMethod.GET, entity, String.class);
    
    return response.getBody();
  }
  
  public String bookingCancelByUser(String customerEmail, String bookingId){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(bidServiceUrl+"/bid/booking/cancel/user?customerEmail="+customerEmail +
        "&bookingId="+bookingId, HttpMethod.GET, entity, String.class);
    
    return response.getBody();
  }
}
