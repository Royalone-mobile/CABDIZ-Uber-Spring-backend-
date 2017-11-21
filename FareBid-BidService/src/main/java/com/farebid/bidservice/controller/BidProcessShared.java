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

public class BidProcessShared implements Runnable{

  private RabbitTemplate rabbitTemplate;
  private ObjectMapper objectMapper;
  private StringRedisTemplate template;
  private List<Driver> driverList;
  private Trip trip;
  
  public BidProcessShared(RabbitTemplate rabbitTemplate, StringRedisTemplate template, ObjectMapper objectMapper, 
      List<Driver> driverList, Trip trip) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper = objectMapper;
    this.template = template;
    this.driverList = driverList;
    this.trip = trip;
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
          BidMessage result = objectMapper.readValue(ops.get("trip/" + trip.getId()), BidMessage.class);
          String json = objectMapper.writeValueAsString(result);
          ops.set("trip/" + trip.getId(), json);
          i++;
        }
        BidMessage result = objectMapper.readValue(ops.get("trip/" + trip.getId()), BidMessage.class);
        System.err.println("deciding winner");
        result = objectMapper.readValue(ops.get("trip/" + trip.getId()), BidMessage.class);
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
      } catch (InterruptedException | IOException e) {
        // TODO
      }
    }
    
  }
  
}
