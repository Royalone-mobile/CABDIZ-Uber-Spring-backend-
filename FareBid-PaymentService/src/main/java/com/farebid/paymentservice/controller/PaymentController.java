package com.farebid.paymentservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farebid.paymentservice.mongo.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {
  
  @Autowired
  PaymentRepository paymentRepository;

  @RequestMapping(value = "/stripe")
  public Boolean stripe(String source, Long amount, String email) {
    RequestOptions requestOptions =
        (new RequestOptionsBuilder()).setApiKey("YOUR-SECRET-KEY").build();
    Map<String, Object> chargeMap = new HashMap<String, Object>();
    chargeMap.put("amount", amount);
    chargeMap.put("currency", "usd");
    chargeMap.put("source", "tok_1234"); // obtained via Stripe.js
    Payment payment = new Payment();
    payment.setAmount(amount);
    payment.setCurrency("usd");
    payment.setSource(source);
    payment.setUserEmail(email);
    try {
      Charge charge = Charge.create(chargeMap, requestOptions);
      if (charge.getPaid() && charge.getAmount() == amount) {
        payment.setSuccess(true);
        paymentRepository.save(payment);
        return true;
      } else {
        // "failed to deposit in stripe"
      }
    } catch (StripeException e) {
      // "failed to deposit in stripe"
      e.printStackTrace();
    }
    payment.setSuccess(false);
    paymentRepository.save(payment);
    return false;
  }
}
