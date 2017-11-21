package com.farebid.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FareBidPaymentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FareBidPaymentServiceApplication.class, args);
  }
}
