package com.farebid.geoservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableDiscoveryClient
public class FareBidGeoServiceApplication implements CommandLineRunner {

  public static final String vacantKeyName = "vacant-location";
  public static final String nonVacantKeyName = "non-vacant-location";
  public static final String sharedKeyName = "shared-location";
  
  @Autowired
  private StringRedisTemplate template;
  
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

  @Override
  public void run(String... args) throws Exception {
    ValueOperations<String, String> ops = this.template.opsForValue();
    String key = "inner-radius";
    ops.set(key, "6000");
    key = "outer-radius";
    ops.set(key, "10000");
    key = "shared-radius";
    ops.set(key, "6000");

    System.out.println("Found key " + key + ", value=" + ops.get(key));
    
    GeoOperations<String, String> geo = this.template.opsForGeo();
    
    geo.geoRemove(vacantKeyName, "test", "test2", "test3", "test4");
    

  }

  public static void main(String[] args) {
    SpringApplication.run(FareBidGeoServiceApplication.class, args);
  }
}
