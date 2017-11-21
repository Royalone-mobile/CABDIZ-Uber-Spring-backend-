package com.farebid.bidservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BidRepository extends MongoRepository<Bid, String>{

}
