package com.farebid.bidservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface  BidRequestRepository extends MongoRepository<BidRequest, String>{

  public BidRequest findByBidId(String bidId);
}
