package com.farebid.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Service
public class FareBidUserDetailsService implements UserDetailsService{

  @Autowired
  UserService userService;
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    com.farebid.webservice.domain.UserLoginInfoDTO user;
    user = userService.getUser(email);
    return new User(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));
  }

}
