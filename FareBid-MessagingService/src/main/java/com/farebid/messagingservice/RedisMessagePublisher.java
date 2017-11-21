package com.farebid.messagingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public class RedisMessagePublisher{

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;
  
  @Autowired
  private ChannelTopic topic;

  public RedisMessagePublisher(final RedisTemplate<String, Object> redisTemplate,
      final ChannelTopic topic) {
    this.redisTemplate = redisTemplate;
    this.topic = topic;
  }

  public void publish(final String message) {
    FareBidMessage mes = new FareBidMessage();
    mes.setEmail("teat");
    mes.setText("test");
    mes.setToken("token");
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }
}
