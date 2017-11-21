package com.farebid.tripservice.controller;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.farebid.tripservice.mongo.TripRepository;

@RestController
@RequestMapping(value = "/trip")
public class TripController {

  private final String bidServiceUrl = "http://bid-service";

  @Autowired
  @LoadBalanced
  RestTemplate restTemplate;

  @Autowired
  TripRepository tripRepository;

  @RequestMapping(value = "/start")
  public String start(String bidId, String driverEmail, Double x, Double y) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);

    BidRequest bidRequest = null;
    try {
      ResponseEntity<BidRequest> response = restTemplate.exchange(
          bidServiceUrl + "/bid/findById/" + bidId, HttpMethod.GET, entity, BidRequest.class);
      bidRequest = response.getBody();
    } catch (Exception e) {
      return null;
    }

    if (!bidRequest.getWinner().getName().equals(driverEmail)) {
      return null;
    }

    // ask confirmation of customer

    Trip trip = new Trip(driverEmail, bidRequest.getCustomerEmail(),
        bidRequest.getWinner().getFee(), x, y, bidRequest.getTargetX(), bidRequest.getTargetY());
    tripRepository.save(trip);

    // start saving driver and customer location

    return trip.getId();
  }


  @RequestMapping(value = "/end")
  public Boolean end(String tripId, String driverEmail, Double x, Double y) {
    Trip trip = tripRepository.findOne(tripId);
    if (trip == null) {
      return false;
    }
    if (trip.getDriverEmail().equals(driverEmail)) {
      return false;
    }

    // ask confirmation of customer

    // compare original distance to real distance and recalculate if needed
    Double finalPrice = 45D;

    trip.setDateFinished(new Date());
    trip.setDateUpdated(trip.getDateFinished());
    trip.setFinalX(x);
    trip.setFinalY(y);
    trip.setFinalPrice(finalPrice);
    trip.setFinished(true);

    return true;
  }
  
  @RequestMapping(value = "/find-by-email")
  public Trip findTripByCustomerEmail(String customerEmail){
    return tripRepository.findByCustomerEmailAndStartedIsTrue(customerEmail);
  }
  
  @RequestMapping(value = "/history/customer")
  public List<Trip> customerTripHistory(String customerEmail){
    return tripRepository.findByCustomerEmailAndFinishedIsTrue(customerEmail);
  }
  
  @RequestMapping(value = "/history/driver")
  public List<Trip> driverTripHistory(String driverEmail){
    return tripRepository.findByDriverEmailAndFinishedIsTrue(driverEmail);
  }
  
  @RequestMapping(value = "/estimated-fee")
  public Double findTripByCustomerEmail(Double distance){
    double feeForMeter = 0.002d; //TODO get from config
    double fee =
        new BigDecimal("" + distance).multiply(new BigDecimal("" + feeForMeter))
        .setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    return fee;
  }
}
