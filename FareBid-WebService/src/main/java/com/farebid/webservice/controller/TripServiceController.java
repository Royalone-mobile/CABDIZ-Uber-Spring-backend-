package com.farebid.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.farebid.webservice.domain.FareBidApiResponse;
import com.farebid.webservice.service.TripService;

@RestController
@RequestMapping(value = "/trip-service")
public class TripServiceController {

  @Autowired
  TripService tripService;
  
  @RequestMapping(value = "/estimated-fee", method = RequestMethod.POST)
  public FareBidApiResponse checkEmail(Double distance){
    FareBidApiResponse response = new FareBidApiResponse();
    if(distance == null || distance < 1){
      response.setSuccess(false);
      response.setErrorMessage("Invalid Distance");
      return response;
    }
    
    Double fee = tripService.getEstimatedFee(distance);
    if(fee == null){
      response.setSuccess(false);
      response.setErrorMessage("An Error Has Occured");
    }else{
      response.setSuccess(true);
      response.setData(fee + "");
    }
    return response;
  }
  
  @RequestMapping(value = "/history/customer", method = RequestMethod.POST)
  public FareBidApiResponse getTripHistoryForCustomer(String customerEmail){
    FareBidApiResponse response = new FareBidApiResponse();
    if(customerEmail == null){
      response.setSuccess(false);
      response.setErrorMessage("Invalid Email");
      return response;
    }
    
    String data = tripService.getTripHistoryForCustomer(customerEmail);
    response.setSuccess(true);
    response.setData(data);
    return response;
  }
  
  @RequestMapping(value = "/history/driver", method = RequestMethod.POST)
  public FareBidApiResponse getTripHistoryForDriver(String driverEmail){
    FareBidApiResponse response = new FareBidApiResponse();
    if(driverEmail == null){
      response.setSuccess(false);
      response.setErrorMessage("Invalid Email");
      return response;
    }
    
    String data = tripService.getTripHistoryForDriver(driverEmail);
    response.setSuccess(true);
    response.setData(data);
    return response;
  }
}
