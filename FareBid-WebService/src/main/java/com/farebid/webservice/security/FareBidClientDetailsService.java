package com.farebid.webservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.farebid.webservice.domain.Client;
import com.farebid.webservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class FareBidClientDetailsService implements ClientDetailsService {

  private final UserService userService;

  private @Value("${authentication.oauth.accessTokenValidityInSeconds}") Integer accessTokenValidityInSeconds;
  private @Value("${authentication.oauth.refreshTokenValidityInSeconds}") Integer refreshTokenValidityInSeconds;

  @Autowired
  public FareBidClientDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ClientDetails loadClientByClientId(String clientType) throws ClientRegistrationException {
    Client client;
    try {
      client = userService.getClient(clientType);
      if (client == null) {
        throw new ClientRegistrationException(clientType);
      }
    } catch (JsonProcessingException e) {
      throw new ClientRegistrationException(clientType, e);
    }
    
    BaseClientDetails clientDetails =
        new BaseClientDetails(clientType, null, "read,write", "password,refresh_token", null);
    clientDetails.setClientSecret(client.getSecret());
    clientDetails.setAccessTokenValiditySeconds(accessTokenValidityInSeconds);
    clientDetails.setRefreshTokenValiditySeconds(refreshTokenValidityInSeconds);
    return clientDetails;
  }

}
