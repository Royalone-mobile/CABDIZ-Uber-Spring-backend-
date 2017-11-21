package com.farebid.geoservice.controller;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomerLocationUpdateProcess implements Runnable{
  
  String customerEmail;
  StringRedisTemplate template;
  RabbitTemplate rabbitTemplate; 
  AmqpAdmin amqpAdmin;
  ExecutorService executor;
  ObjectMapper objectMapper;
  
  public CustomerLocationUpdateProcess(String customerEmail, StringRedisTemplate template, 
      RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin, ExecutorService executor, ObjectMapper objectMapper){
    this.customerEmail = customerEmail;
    this.template = template;
    this.rabbitTemplate = rabbitTemplate;
    this.amqpAdmin = amqpAdmin;
    this.executor = executor;
    this.objectMapper = objectMapper;
  }

  @Override
  public void run() {
    Queue q = new Queue("driver_location_update/" + customerEmail);
    amqpAdmin.declareQueue(q);
    GeoOperations<String, String> geo = this.template.opsForGeo();
      
    while (!Thread.interrupted()) {
      Message m = rabbitTemplate.receive(q.getName(), -1); //Wait indefinetly for a message
      if (m != null) {
        String json = new String(m.getBody());
        DriverLocationUpdate locationUpdate = null;
        try {
          locationUpdate = objectMapper.readValue(json, DriverLocationUpdate.class);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } 

        Point p = new Point(locationUpdate.getX(), locationUpdate.getY());
        GeoLocation<String> loc = new GeoLocation<String>(customerEmail, p);
        if(locationUpdate.getVacant()){
          geo.geoAdd("vacant-location", loc);
        }else{
          geo.geoAdd("non-vacant-location", loc);
        } 
      }
    }
    
  }
  
  public void start(){
    executor.execute(this);
  }

}
