package com.farebid.bidservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class FareBidBidServiceApplication  {

  @LoadBalanced   
  @Bean
  RestTemplate restTemplate() {
      return new RestTemplate();
  }

  @Bean
  public ExecutorService executorService() {
    return Executors.newCachedThreadPool();
  }
  
  @Bean
  public ObjectMapper objectMapper(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  @Component
  public static class Runner implements CommandLineRunner {
    
    private RabbitTemplate rabbitTemplate;
    private AmqpAdmin amqpAdmin;
    private StringRedisTemplate template;
    private ExecutorService executer;
    
    @Autowired
    public Runner(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin,
        StringRedisTemplate template, ExecutorService executer){
      this.rabbitTemplate = rabbitTemplate;
      this.amqpAdmin = amqpAdmin;
      this.template = template;
      this.executer = executer;
    }
    
    @Override
    public void run(String... args) throws Exception {
      FareBidAmqbListener listener1 = new FareBidAmqbListener(rabbitTemplate, "driver/test", amqpAdmin, executer);
      listener1.start();
      FareBidAmqbListener listener2 = new FareBidAmqbListener(rabbitTemplate, "driver/test2", amqpAdmin, executer);
      listener2.start();
      FareBidAmqbListener listener3 = new FareBidAmqbListener(rabbitTemplate, "driver/test3", amqpAdmin, executer);
      listener3.start();
      FareBidAmqbListener listener4 = new FareBidAmqbListener(rabbitTemplate, "driver/test4", amqpAdmin, executer);
      listener4.start();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(FareBidBidServiceApplication.class, args);
  }
}
