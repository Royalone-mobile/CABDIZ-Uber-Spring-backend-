package com.farebid.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.farebid.userservice.entity.PaymentInfo;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long>{
  
  PaymentInfo findByEmail(String email);

  @Modifying
  @Transactional
  @Query("update PaymentInfo p set p.cardNumber = :card_number, p.cardExpiry = :card_expiry, "
      + "p.cardCvc = :card_cvc, p.stripeId = :stripe_id where p.email = :email")
  int update(@Param("email") String email, @Param("card_number") String card_number, 
      @Param("card_expiry") String card_expiry, @Param("card_cvc") String card_cvc, @Param("stripe_id") String stripe_id);
}
