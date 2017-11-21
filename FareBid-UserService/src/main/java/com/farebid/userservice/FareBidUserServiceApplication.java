package com.farebid.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;

import com.farebid.userservice.entity.Client;
import com.farebid.userservice.entity.Role;
import com.farebid.userservice.entity.User;
import com.farebid.userservice.repository.ClientRepository;
import com.farebid.userservice.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class FareBidUserServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FareBidUserServiceApplication.class, args);
  }
}
