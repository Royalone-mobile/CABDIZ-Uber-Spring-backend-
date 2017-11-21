package com.farebid.userservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.farebid.userservice.entity.Role;
import com.farebid.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);
  
  List<User> findByRole(@Param("role") Role role);
  
  @Modifying
  @Transactional
  @Query("update User u set u.forename = :first_name, u.surename = :last_name, u.phone = :phone_number where u.email = :email")
  int updateUserDetail(@Param("email") String email, @Param("first_name") String first_name, 
      @Param("last_name") String last_name, @Param("phone_number") String phone_number);
}
