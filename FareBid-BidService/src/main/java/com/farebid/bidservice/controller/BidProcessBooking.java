package com.farebid.bidservice.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.farebid.bidservice.mongo.BidRequest;
import com.farebid.bidservice.mongo.BidRequestRepository;
import com.farebid.bidservice.mongo.Booking;
import com.farebid.bidservice.mongo.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BidProcessBooking implements Runnable{

  private RabbitTemplate rabbitTemplate;
  private ObjectMapper objectMapper;
  private StringRedisTemplate template;
  private String bidId;
  private List<Driver> driverList;
  private BidRequest bidRequest;
  private BidRequestRepository bidRequestRepository;
  private BookingRepository bookingRepository;
  private Booking b;
  
  public BidProcessBooking(RabbitTemplate rabbitTemplate, StringRedisTemplate template, ObjectMapper objectMapper, 
      String bidId, List<Driver> driverList, BidRequest bidRequest, BidRequestRepository bidRequestRepository, 
      BookingRepository bookingRepository, Booking b) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
    this.template = template;
    this.bidId = bidId;
    this.driverList = driverList;
    this.bidRequest = bidRequest;
    this.bidRequestRepository = bidRequestRepository;
    this.bookingRepository = bookingRepository;
    this.b = b;
  }
  
  @Override
  public void run() {
    while (!Thread.interrupted()) {
      try {
        ValueOperations<String, String> ops = template.opsForValue();
        // Wait 60 second for bidding process to finish
        System.err.println("started bid finalizing");
        int i = 0;
        while(i < 3){
          System.err.println("wait 20 seconds to check rejecting users");
          Thread.sleep(20 * 1000);
          BidMessage result = objectMapper.readValue(ops.get("bid/" + bidId), BidMessage.class);
          List<Driver> shortList = removeRejectedDrivers(result, ops);
          result.setShortListedDrivers(shortList);
          String json = objectMapper.writeValueAsString(result);
          ops.set("bid/" + bidId, json);
          for (Driver driver : shortList) {
            rabbitTemplate.convertAndSend("driver/" + driver.getName(), json);
          }
          if(shortList.size() == 0){
            break;
          }
          i++;
        }
        BidMessage result = objectMapper.readValue(ops.get("bid/" + bidId), BidMessage.class);
        System.err.println("deciding winner");
        result = objectMapper.readValue(ops.get("bid/" + bidId), BidMessage.class);
        result.setFinished(true);
        double lowestFee = Double.MAX_VALUE;
        Driver winner = null;
        for (Driver driver : result.getShortListedDrivers()) {
          ops.set("driverShortlist/" + driver.getName(), "", 1, TimeUnit.MILLISECONDS);
          if(driver.isAccepted() && driver.getFee() < lowestFee) {
            winner = driver;
            lowestFee = driver.getFee();
          }
        }
        result.setWinner(winner);
        bidRequest.setWinner(winner);
        bidRequestRepository.save(bidRequest);
        System.err.println("sending message");
        rabbitTemplate.convertAndSend(bidId, objectMapper.writeValueAsString(result));
      } catch (InterruptedException | IOException e) {
        // TODO
      }
    }
  }
  
  private List<Driver> removeRejectedDrivers(BidMessage result, ValueOperations<String, String> ops ){
    List<Driver> shortList = result.getShortListedDrivers();
    int a = 0;
    Iterator<Driver> shortListIt = shortList.iterator();
    while(shortListIt.hasNext()){
      Driver driver = shortListIt.next();
      if(!driver.isAccepted()){
        shortListIt.remove();
        ops.set("driverShortlist/" + driver.getName(), "", 1, TimeUnit.MILLISECONDS);
        a++;
      }
    }
    Iterator<Driver> driverListIt = driverList.iterator();
    while(driverListIt.hasNext() && a > 0){
      Driver driver = driverListIt.next();
      Calendar min = Calendar.getInstance();
      min.setTime(b.getTripStart());
      min.add(Calendar.MINUTE, -30);
      Calendar max = Calendar.getInstance();
      max.setTime(b.getTripStart());
      max.add(Calendar.MINUTE, 30);
      List<Booking> existingBookings = bookingRepository
          .findByTripStartBetweenAndWinnerNameAndFinalizedIsTrue(min.getTime(), max.getTime(), 
              driver.getName());
      String alreadyShortListedName = ops.get("driverShortlist/" + driver.getName());
      if(StringUtils.isEmpty(alreadyShortListedName) && existingBookings.size() == 0){
        shortList.add(driver);
        bidRequest.getDrivers().add(driver);
        ops.set("driverShortlist/" + driver.getName(), driver.getName());
        driverListIt.remove();
        a--;
      }
    }
    return shortList;
  }

}
