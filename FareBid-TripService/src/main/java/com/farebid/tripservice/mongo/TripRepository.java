package com.farebid.tripservice.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.farebid.tripservice.controller.Trip;

public interface TripRepository extends MongoRepository<Trip, String>{

  List<Trip> findByCustomerEmailAndFinishedIsTrue(String customerEmail);
  List<Trip> findByDriverEmailAndFinishedIsTrue(String customerEmail);
  Trip findByCustomerEmailAndStartedIsTrue(String customerEmail);
}
