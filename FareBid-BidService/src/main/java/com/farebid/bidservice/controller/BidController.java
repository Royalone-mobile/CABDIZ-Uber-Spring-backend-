package com.farebid.bidservice.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.farebid.bidservice.mongo.Bid;
import com.farebid.bidservice.mongo.BidRepository;
import com.farebid.bidservice.mongo.BidRequest;
import com.farebid.bidservice.mongo.BidRequestRepository;
import com.farebid.bidservice.mongo.Booking;
import com.farebid.bidservice.mongo.BookingCancellation;
import com.farebid.bidservice.mongo.BookingCancellationRepository;
import com.farebid.bidservice.mongo.BookingRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/bid")
public class BidController {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private StringRedisTemplate template;

  @Autowired
  private AmqpAdmin amqpAdmin;

  @Autowired
  ExecutorService executorService;

  private final String geoServiceUrl = "http://geo-service";
  
  private final String tripServiceUrl = "http://trip-service";

  @Autowired
  @LoadBalanced
  RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BidRequestRepository bidRequestRepository;

  @Autowired
  BidRepository bidRepository;

  @Autowired
  BookingRepository bookingRepository;

  @Autowired
  BookingCancellationRepository bookingCancellationRepository;

  @RequestMapping(value = "/initiate")
  public String initiate(String customerEmail, Double x, Double y, Double targetX, Double targetY,
      Boolean shared, Double distance, Double defaultFee) throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    
    if (shared) {
      
      ParameterizedTypeReference<List<Driver>> typeRef =
          new ParameterizedTypeReference<List<Driver>>() {};
      ResponseEntity<List<Driver>> driverListResponse = restTemplate.exchange(
          geoServiceUrl + "/geo/driver_list_shared?x=" + x + "&y=" + y, HttpMethod.GET, entity, typeRef);
      List<Driver> driverList = driverListResponse.getBody();
      for (Driver driver : driverList) {
        ResponseEntity<Trip> tripResponse = restTemplate.exchange(
            tripServiceUrl + "/trip/find-by-email?customerEmail=" + driver.getName(), HttpMethod.GET, entity, Trip.class);
        Trip trip = tripResponse.getBody();
        BidProcessShared process = new BidProcessShared(rabbitTemplate, template, objectMapper, driverList, trip);
        executorService.execute(process);
   
        return trip.getId();
      }
    }

    // generate bid id;
    String bidId = UUID.randomUUID().toString();

    // stats
    BidRequest bidRequest =
        new BidRequest(customerEmail, x, y, targetX, targetY, bidId, defaultFee, shared);
    Queue q = new Queue(bidId);
    amqpAdmin.declareQueue(q);
    
    

    ValueOperations<String, String> ops = template.opsForValue();

    // shortlist drivers
    ParameterizedTypeReference<List<Driver>> typeRef =
        new ParameterizedTypeReference<List<Driver>>() {};
    ResponseEntity<List<Driver>> driverListResponse = restTemplate.exchange(
        geoServiceUrl + "/geo/driver_list?x=" + x + "&y=" + y, HttpMethod.GET, entity, typeRef);
    List<Driver> driverList = driverListResponse.getBody();
    if (driverList.size() > 0) {
      throw new RuntimeException("No driver");
    }
    BidMessage mes = new BidMessage();
    mes.setId(bidId);
    List<Driver> shortList = new ArrayList<Driver>();
    Iterator<Driver> it = driverList.iterator();
    int i = 0;
    while (it.hasNext()) {
      Driver driver = it.next();
      int discountPercentage = new Random().nextInt(10);// TODO calculate based on individual
      // settings
      double percentage = (100 - discountPercentage) / 100d;
      driver
          .setFee(new BigDecimal("" + defaultFee).multiply(new BigDecimal("" + percentage)).doubleValue());
      Calendar min = Calendar.getInstance();
      min.add(Calendar.MINUTE, -30);
      Calendar max = Calendar.getInstance();
      max.add(Calendar.MINUTE, 30);
      List<Booking> existingBookings =
          bookingRepository.findByTripStartBetweenAndWinnerNameAndFinalizedIsTrue(min.getTime(),
              max.getTime(), driver.getName());
      String alreadyShortListedName = ops.get("driverShortlist/" + driver.getName());
      if (i < 3 && StringUtils.isEmpty(alreadyShortListedName) && existingBookings.size() == 0) {
        it.remove();
        shortList.add(driver);
        ops.set("driverShortlist/" + driver.getName(), driver.getName());
        // stats
        bidRequest.getDrivers().add(driver);
      }
      i++;
    }
    mes.setDefaultFee(defaultFee);
    mes.setTargetX(targetX);
    mes.setTargetY(targetY);
    mes.setShortListedDrivers(shortList);
    String json = objectMapper.writeValueAsString(mes);
    ops.set("bid/" + bidId, json);
    for (Driver driver : shortList) {
      rabbitTemplate.convertAndSend("driver/" + driver.getName(), json);
    }
    BidProcess bid = new BidProcess(rabbitTemplate, template, objectMapper, bidId, driverList,
        bidRequest, bidRequestRepository, bookingRepository);
    executorService.execute(bid);
    return bidId;
  }

  @RequestMapping(value = "/{bidId}", method = RequestMethod.POST)
  public void bid(String customerEmail, Double amount, @PathVariable(name = "bidId") String bidId)
      throws JsonParseException, JsonMappingException, IOException {
    ValueOperations<String, String> ops = template.opsForValue();
    BidMessage mes = objectMapper.readValue(ops.get("bid/" + bidId), BidMessage.class);
    List<Driver> driverList = mes.getShortListedDrivers();
    for (Driver driver : driverList) {
      if (driver.getName().equals(customerEmail)) {
        driver.setAccepted(true);
        driver.setFee(amount);
        // stats
        Bid bid = new Bid(customerEmail, amount);
        bidRepository.save(bid);
      }
    }
    String json = objectMapper.writeValueAsString(mes);
    ops.set("bid/" + bidId, json);
    for (Driver driver : driverList) {
      rabbitTemplate.convertAndSend("driver/" + driver.getName(), json);
    }
  }

  @RequestMapping(value = "/findById/{bidId}")
  public BidRequest findById(@PathVariable(name = "bidId") String bidId) {
    return bidRequestRepository.findByBidId(bidId);
  }


  @RequestMapping(value = "/booking")
  public String booking(String customerEmail, Double x, Double y, Double targetX, Double targetY,
      String tripStart, Boolean shared) throws ParseException {
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Date tripStartDate = df.parse(tripStart);
    Calendar now = Calendar.getInstance();
    Calendar start = Calendar.getInstance();
    start.setTime(tripStartDate);
    long diff = start.getTimeInMillis() - now.getTimeInMillis();
    if (diff < 15 * 60000 || diff > 24 * 60 * 60000) {
      throw new RuntimeException();
    }
    Booking booking = new Booking(customerEmail, x, y, targetX, targetY, tripStartDate, shared);
    booking = bookingRepository.save(booking);
    return booking.getId();
  }

  @RequestMapping(value = "/booking/cancel/user")
  public void bookingCancelByUser(String customerEmail, String bookingId) {
    Booking booking = bookingRepository.findOne(bookingId);
    booking.setBiddingStarted(true);
    booking.setDateCancelled(new Date());
    bookingRepository.save(booking);

    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());

    Calendar tripStart = Calendar.getInstance();
    cal.setTime(booking.getTripStart());
    if (tripStart.getTimeInMillis() - cal.getTimeInMillis() > 35 * 60 * 1000) {
      // do nothing
    } else {
      // cancellation fee
    }
  }

  @RequestMapping(value = "/booking/cancel/driver")
  public void bookingCancelByDriver(String driverEmail, String bidId) {
    Booking booking = bookingRepository.findOne(bidId);

    if (booking.getWinner().getName().equals(driverEmail) || booking.getDateCancelled() != null) {
      throw new RuntimeException();
    }

    booking.setFinalized(false);
    booking.setWinner(null);

    Iterator<Driver> it = booking.getDrivers().iterator();
    while (it.hasNext()) {
      Driver d = it.next();
      if (d.getName().equals(driverEmail)) {
        it.remove();
      }
    }

    bookingRepository.save(booking);

    BookingCancellation bookingCancellation = new BookingCancellation();
    bookingCancellation.setBookingId(booking.getId());
    bookingCancellation.setDriverEmail(driverEmail);
    bookingCancellationRepository.save(bookingCancellation);
    // TODO send to other drivers with same rate

    // will be relisted automatically
  }

  @Scheduled(fixedRate = 60000)
  public void processBookings() throws JsonProcessingException {
    System.err.println("test etsse");

    Calendar now = Calendar.getInstance();
    Booking booking =
        new Booking("sdfbjm", 40.980258, 29.102132, 40.983708D, 29.115221D, now.getTime(), false);
    bookingRepository.save(booking);
    now.add(Calendar.MINUTE, 30);
    List<Booking> bookings =
        bookingRepository.findByTripStartLessThanAndBiddingStartedIsFalse(now.getTime());
    for (Booking b : bookings) {
      System.err.println(b.getTripStart());
      // TODO do not delete
      bookingRepository.delete(b);
      biddingForBooking(b);
    }
  }

  private String biddingForBooking(Booking b) throws JsonProcessingException {
    // generate bid id;
    String bidId = UUID.randomUUID().toString();

    // Calculate distance and fee
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<String>(headers);
    ResponseEntity<Double> response =
        restTemplate.exchange(
            geoServiceUrl + "/geo/distance?x=" + b.getX() + "&y=" + b.getY() + "&targetX="
                + b.getTargetX() + "&targetY=" + b.getTargetY(),
            HttpMethod.GET, entity, Double.class);
    double distance = response.getBody();
    double feeForMeter = 0.02d;
    double fee =
        new BigDecimal("" + distance).multiply(new BigDecimal("" + feeForMeter)).doubleValue();
    // stats
    BidRequest bidRequest = new BidRequest(b.getCustomerEmail(), b.getX(), b.getY(), b.getTargetX(),
        b.getTargetY(), bidId, fee, b.getShared());
    Queue q = new Queue(bidId);
    amqpAdmin.declareQueue(q);

    ValueOperations<String, String> ops = template.opsForValue();

    // shortlist drivers
    ParameterizedTypeReference<List<Driver>> typeRef =
        new ParameterizedTypeReference<List<Driver>>() {};
    ResponseEntity<List<Driver>> driverListResponse =
        restTemplate.exchange(geoServiceUrl + "/geo/driver_list?x=" + b.getX() + "&y=" + b.getY(),
            HttpMethod.GET, entity, typeRef);
    List<Driver> driverList = driverListResponse.getBody();
    if (driverList.size() > 0) {
      throw new RuntimeException("No driver");
    }
    BidMessage mes = new BidMessage();
    mes.setId(bidId);
    mes.setTripStart(b.getTripStart());
    List<Driver> shortList = new ArrayList<Driver>();
    Iterator<Driver> it = driverList.iterator();
    int i = 0;
    Calendar min = Calendar.getInstance();
    min.setTime(b.getTripStart());
    min.add(Calendar.MINUTE, -30);
    Calendar max = Calendar.getInstance();
    max.setTime(b.getTripStart());
    max.add(Calendar.MINUTE, 30);
    while (it.hasNext()) {
      Driver driver = it.next();
      int discountPercentage = new Random().nextInt(10);// TODO calculate based on individual
      // settings
      double percentage = (100 - discountPercentage) / 100d;
      driver
          .setFee(new BigDecimal("" + fee).multiply(new BigDecimal("" + percentage)).doubleValue());


      List<Booking> existingBookings =
          bookingRepository.findByTripStartBetweenAndWinnerNameAndFinalizedIsTrue(min.getTime(),
              max.getTime(), driver.getName());
      String alreadyShortListedName = ops.get("driverShortlist/" + driver.getName());
      if (i < 3 && StringUtils.isEmpty(alreadyShortListedName) && existingBookings.size() == 0) {
        it.remove();
        shortList.add(driver);
        ops.set("driverShortlist/" + driver.getName(), driver.getName());
        // stats
        bidRequest.getDrivers().add(driver);
      }
      i++;
    }
    mes.setDefaultFee(fee);
    mes.setTargetX(b.getTargetX());
    mes.setTargetY(b.getTargetY());
    mes.setShortListedDrivers(shortList);
    String json = objectMapper.writeValueAsString(mes);
    ops.set("bid/" + bidId, json);
    for (Driver driver : shortList) {
      rabbitTemplate.convertAndSend("driver/" + driver.getName(), json);
    }
    BidProcessBooking bid = new BidProcessBooking(rabbitTemplate, template, objectMapper, bidId,
        driverList, bidRequest, bidRequestRepository, bookingRepository, b);
    executorService.execute(bid);
    return bidId;
  }
}
