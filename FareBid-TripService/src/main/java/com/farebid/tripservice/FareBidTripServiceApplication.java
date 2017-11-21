package com.farebid.tripservice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.client.RestTemplate;

import com.farebid.tripservice.controller.Trip;
import com.farebid.tripservice.mongo.TripRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class FareBidTripServiceApplication{

  @LoadBalanced
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
  @Autowired
  TripRepository tripRepository;


  public static void main(String[] args) {
    SpringApplication.run(FareBidTripServiceApplication.class, args);
  }
}
