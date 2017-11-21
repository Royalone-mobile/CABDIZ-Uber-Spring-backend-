package com.farebid.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.farebid.userservice.entity.Client;
import com.farebid.userservice.repository.ClientRepository;

@RestController
@RequestMapping(value = "/client")
public class ClientController {

  @Autowired
  ClientRepository clientRepository;
  
  @RequestMapping(value = "")
  public Client getClient(String clientType){
    return clientRepository.findByClientType(clientType);
  }
}
