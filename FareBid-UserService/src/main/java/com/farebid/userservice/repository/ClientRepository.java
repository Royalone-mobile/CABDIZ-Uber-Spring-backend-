package com.farebid.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.farebid.userservice.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

  Client findByClientType(String clientType);
}
