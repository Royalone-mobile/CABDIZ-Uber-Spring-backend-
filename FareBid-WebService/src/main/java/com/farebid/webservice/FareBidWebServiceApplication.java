package com.farebid.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class FareBidWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FareBidWebServiceApplication.class, args);
	}
	
	@LoadBalanced   
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
