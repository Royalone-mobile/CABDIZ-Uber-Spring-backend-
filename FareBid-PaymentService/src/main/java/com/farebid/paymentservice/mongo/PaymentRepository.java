package com.farebid.paymentservice.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.farebid.paymentservice.controller.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String>{

}
