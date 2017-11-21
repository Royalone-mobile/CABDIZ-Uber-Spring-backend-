package com.farebid.geoservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farebid.geoservice.FareBidGeoServiceApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/geo")
public class GeoController {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private StringRedisTemplate template;

  @Autowired
  private AmqpAdmin amqpAdmin;

  @Autowired
  ExecutorService executorService;
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @RequestMapping(value = "/driver_list_shared")
  public List<Driver> getDriverShortListShared(Double x, Double y) {
    List<Driver> resultList = new ArrayList<>();
    ValueOperations<String, String> ops = this.template.opsForValue();
    GeoOperations<String, String> geo = this.template.opsForGeo();
    String key = "shared-radius";
    String radiusString = ops.get(key);
    Point p = new Point(x, y);
    int radius = Integer.parseInt(radiusString);
    // search vacant
    Circle c = new Circle(p, radius);
    GeoResults<GeoLocation<String>> result =
        geo.geoRadius(FareBidGeoServiceApplication.sharedKeyName, c);

    String tempKey = UUID.randomUUID().toString();
    geo.geoAdd(tempKey, new GeoLocation<String>("customer", p));
    result.forEach(l -> {
      GeoLocation<String> loc = new GeoLocation<String>(l.getContent().getName(),
          geo.geoPos(FareBidGeoServiceApplication.sharedKeyName, l.getContent().getName()).get(0));
      geo.geoAdd(tempKey, loc);
      Distance d = geo.geoDist(tempKey, l.getContent().getName(), "customer");
      resultList.add(new Driver(l.getContent().getName(), d.getValue()));
      geo.geoRemove(tempKey, l.getContent().getName());
    });
    geo.geoRemove(tempKey, "customer");

    resultList.sort((Driver d1, Driver d2) -> d1.getDistance().compareTo(d2.getDistance()));

    return resultList;
  }

  @RequestMapping(value = "/driver_list")
  public List<Driver> getDriverShortList(Double x, Double y) {
    List<Driver> resultList = new ArrayList<>();
    ValueOperations<String, String> ops = this.template.opsForValue();
    GeoOperations<String, String> geo = this.template.opsForGeo();
    String key = "inner-radius";
    String radiusString = ops.get(key);
    Point p = new Point(x, y);
    int radius = Integer.parseInt(radiusString);
    // search vacant
    Circle c = new Circle(p, radius);
    GeoResults<GeoLocation<String>> result =
        geo.geoRadius(FareBidGeoServiceApplication.vacantKeyName, c);

    String tempKey = UUID.randomUUID().toString();
    geo.geoAdd(tempKey, new GeoLocation<String>("customer", p));
    result.forEach(l -> {
      GeoLocation<String> loc = new GeoLocation<String>(l.getContent().getName(),
          geo.geoPos(FareBidGeoServiceApplication.vacantKeyName, l.getContent().getName()).get(0));
      geo.geoAdd(tempKey, loc);
      Distance d = geo.geoDist(tempKey, l.getContent().getName(), "customer");
      resultList.add(new Driver(l.getContent().getName(), d.getValue()));
      geo.geoRemove(tempKey, l.getContent().getName());
    });


    // search non vacant
    c = new Circle(p, radius);
    result = geo.geoRadius(FareBidGeoServiceApplication.nonVacantKeyName, c);
    result.forEach(l -> {
      GeoLocation<String> loc = new GeoLocation<String>(l.getContent().getName(), geo
          .geoPos(FareBidGeoServiceApplication.nonVacantKeyName, l.getContent().getName()).get(0));
      geo.geoAdd(tempKey, loc);
      Distance d = geo.geoDist(tempKey, l.getContent().getName(), "customer");

      double remaininDistance = new Random().nextInt(6000);
      resultList.add(new Driver(l.getContent().getName(), d.getValue() + remaininDistance));
      geo.geoRemove(tempKey, l.getContent().getName());
    });


    // search outer if needed
    if (resultList.size() < 9) {
      resultList.clear();
      key = "outer-radius";
      radiusString = ops.get(key);
      radius = Integer.parseInt(radiusString);
      c = new Circle(p, radius);
      result = geo.geoRadius(FareBidGeoServiceApplication.vacantKeyName, c);
      geo.geoAdd(tempKey, new GeoLocation<String>("customer", p));
      result.forEach(l -> {
        GeoLocation<String> loc = new GeoLocation<String>(l.getContent().getName(), geo
            .geoPos(FareBidGeoServiceApplication.vacantKeyName, l.getContent().getName()).get(0));
        geo.geoAdd(tempKey, loc);
        Distance d = geo.geoDist(tempKey, l.getContent().getName(), "customer");
        resultList.add(new Driver(l.getContent().getName(), d.getValue()));
        geo.geoRemove(tempKey, l.getContent().getName());
      });

      result = geo.geoRadius(FareBidGeoServiceApplication.nonVacantKeyName, c);
      result.forEach(l -> {
        GeoLocation<String> loc = new GeoLocation<String>(l.getContent().getName(),
            geo.geoPos(FareBidGeoServiceApplication.nonVacantKeyName, l.getContent().getName())
                .get(0));
        geo.geoAdd(tempKey, loc);
        Distance d = geo.geoDist(tempKey, l.getContent().getName(), "customer");

        double remainingDistance = new Random().nextInt(6000);
        resultList.add(new Driver(l.getContent().getName(), d.getValue() + remainingDistance));
        geo.geoRemove(tempKey, l.getContent().getName());
      });
    }

    geo.geoRemove(tempKey, "customer");

    resultList.sort((Driver d1, Driver d2) -> d1.getDistance().compareTo(d2.getDistance()));

    if(resultList.size() > 9){
      return resultList.subList(0, 9);
    }
    return resultList;
  }
  
  @RequestMapping(value = "/distance")
  public double getDistance(Double x, Double y, Double targetX, Double targetY) {
    GeoOperations<String, String> geo = this.template.opsForGeo();
    String tempKey = UUID.randomUUID().toString();
    geo.geoAdd(tempKey, new GeoLocation<String>("customer", new Point(x, y)));
    geo.geoAdd(tempKey, new GeoLocation<String>("target", new Point(targetX, targetY)));
    return geo.geoDist(tempKey, "customer", "target").getValue();   
  }
  
  @RequestMapping(value = "/update_location/driver")
  public void driverLocationUpdate(String customerEmail) {
    DriverLocationUpdateProcess driverLocationUpdateProcess = new DriverLocationUpdateProcess(customerEmail, template, 
        rabbitTemplate, amqpAdmin, executorService, objectMapper);
    driverLocationUpdateProcess.start();
  }
  
  @RequestMapping(value = "/update_location/customer")
  public void customerLocationUpdate(String customerEmail) {
    CustomerLocationUpdateProcess customerLocationUpdateProcess = new CustomerLocationUpdateProcess(customerEmail, template, 
        rabbitTemplate, amqpAdmin, executorService, objectMapper);
    customerLocationUpdateProcess.start();
  }
}
