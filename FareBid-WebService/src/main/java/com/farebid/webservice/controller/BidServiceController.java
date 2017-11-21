package com.farebid.webservice.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.farebid.webservice.service.BidService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value = "/bid-service")
public class BidServiceController {
  
  @Autowired
  BidService bidService;
  
  @RequestMapping(value = "/initiate")
  public String initiate(Principal principal, Double x, Double y, Double targetX, Double targetY, 
      Double distance, Double defaultFee){
    return bidService.initiateBid(principal.getName(), x, y, targetX, targetY, distance, defaultFee);
  }
  
  @RequestMapping(value = "/{bidId}", method = RequestMethod.POST)
  public void bid(Principal principal, Double amount, @PathVariable(name="bidId") String bidId) throws JsonProcessingException {
    bidService.bid(principal.getName(), amount, bidId);
  }
  
  @RequestMapping(value = "/booking")
  public String booking(Principal principal, Double x, Double y, Double targetX, Double targetY, String tripStart) {
    return bidService.booking(principal.getName(), x, y, targetX, targetY, tripStart);
  }
  
  @RequestMapping(value = "/booking/cancel/user")
  public String bookingCancelUser(Principal principal, String bookingId) {
    return bidService.bookingCancelByUser(principal.getName(), bookingId);
  }
}
