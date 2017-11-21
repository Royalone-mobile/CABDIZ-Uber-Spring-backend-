package com.farebid.messagingservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.farebid.messagingservice.FareBidMessage;

public interface FareBidMessageRepository extends MongoRepository<FareBidMessage, String>{

}
