package com.farebid.bidservice.mongo;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String>{
    
  List<Booking> findByTripStartLessThanAndBiddingStartedIsFalse(Date tripDate);
  
  List<Booking> findByTripStartBetweenAndWinnerNameAndFinalizedIsTrue(Date minDate, Date maxDate, String winnerName);
}
