package com.farebid.bidservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingCancellationRepository extends MongoRepository<BookingCancellation, String>{

}
