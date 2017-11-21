package com.farebid.messagingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class FareBidMessagingServiceApplication {

  @Bean
  MessageListenerAdapter messageListener() {
    return new MessageListenerAdapter(new RedisMessageSubscriber());
  }

  @Bean
  RedisMessageListenerContainer redisContainer() {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(jedisConnectionFactory());
    container.addMessageListener(messageListener(), topic());
    return container;
  }

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  ChannelTopic topic() {
    return new ChannelTopic("pushQueue");
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisConnectionFactory());
    template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
    return template;
  }

  @Bean
  RedisMessagePublisher redisPublisher() {
    return new RedisMessagePublisher(redisTemplate(), topic());
  }
  
  @Bean
  public ObjectMapper objectMapper(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }

  public static void main(String[] args) {
    SpringApplication.run(FareBidMessagingServiceApplication.class, args);
  }

  @Component
  public static class Runner implements CommandLineRunner {

    private RedisMessagePublisher publisher;

    @Autowired
    public Runner(RedisMessagePublisher publisher) {
      this.publisher = publisher;
    }

    @Override
    public void run(String... arg0) throws Exception {
      publisher.publish("Test");

    }

  }
}
