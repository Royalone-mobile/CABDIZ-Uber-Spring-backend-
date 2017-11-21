package com.farebid.userservice.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.farebid.userservice.dto.UserLoginInfoDTO;
import com.farebid.userservice.entity.Dispute;
import com.farebid.userservice.entity.PaymentInfo;
import com.farebid.userservice.entity.Role;
import com.farebid.userservice.entity.User;
import com.farebid.userservice.entity.UserFile;
import com.farebid.userservice.entity.UserFileType;
import com.farebid.userservice.repository.DisputeRepository;
import com.farebid.userservice.repository.PaymentInfoRepository;
import com.farebid.userservice.repository.UserFileRepository;
import com.farebid.userservice.repository.UserRepository;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  UserRepository userRepository;
  
  @Autowired
  UserFileRepository userFileRepository;
  
  @Autowired
  PaymentInfoRepository paymentInfoRepository;
  
  @Autowired
  DisputeRepository disputeRepository;

  @Autowired
  private JavaMailSender javaMailSender;

  @RequestMapping(value = "")
  public User getUser(String email) {
    return userRepository.findByEmail(email);
  }
  
  @RequestMapping(value = "/get-payment-info", method = RequestMethod.GET)
  public PaymentInfo getPaymentInfo(String email) {
    return paymentInfoRepository.findByEmail(email);
  }
  
  @RequestMapping(value = "/register-user", method = RequestMethod.GET)
  public ResponseEntity<User> registerUser(String email, String first_name, String last_name, String phone_number, 
      String password, String card_number, String card_expiry, String card_cvc, String postal_code, String stripe_id, 
      String fileName){
    
    if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    User existinguser = userRepository.findByEmail(email);
    if (existinguser != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    User user = new User();
    user.setEmail(email);
    user.setForename(first_name);
    user.setSurename(last_name);
    user.setPassword(password);
    user.setPhone(phone_number);
    user.setPostalCode(postal_code);
    user.setRole(Role.ROLE_USER);
    
    user = userRepository.save(user);
    
    PaymentInfo paymentInfo = new PaymentInfo();
    paymentInfo.setCardCvc(card_cvc);
    paymentInfo.setCardExpiry(card_expiry);
    paymentInfo.setCardNumber(card_number);
    paymentInfo.setStripeId(stripe_id);
    paymentInfo.setUserId(user.getId());
    paymentInfo.setEmail(email);
    
    paymentInfoRepository.save(paymentInfo);
   
    int updateCount = userFileRepository.update(email, user.getId(), fileName);
    System.err.println("updateCount: " + updateCount + "for file name: " + fileName);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }
  
  @RequestMapping(value = "/update-user", method = RequestMethod.GET)
  public ResponseEntity<?> updateUser(String email, String first_name, String last_name, 
      String phone_number, String stripe_id){
    
    if (StringUtils.isBlank(email)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    userRepository.updateUserDetail(email, first_name, last_name, phone_number);
    
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
  
  @RequestMapping(value = "/update-payment-info", method = RequestMethod.GET)
  public ResponseEntity<?> updatePaymentInfo(String email, String card_number, String card_expiry, 
      String card_cvc, String stripe_id){
    
    if (StringUtils.isBlank(email)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    paymentInfoRepository.update(email, card_number, card_expiry, card_cvc, stripe_id);
    
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

  @RequestMapping(value = "/upload-file", method = RequestMethod.GET)
  @Transactional
  public ResponseEntity<?> uploadFile(String fileName, String fileType){
    if (StringUtils.isBlank(fileName) || StringUtils.isBlank(fileType)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
 
    try{
      UserFileType.valueOf(fileType);
    }catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    UserFile file = new  UserFile();
    file.setFileName(fileName);
    file.setFileType(UserFileType.valueOf(fileType));

    userFileRepository.save(file);    
    System.err.println("file saved: " + fileName);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }
  
  @RequestMapping(value = "/get-file", method = RequestMethod.GET)
  @Transactional
  public ResponseEntity<UserFile> getFile(String email, String fileType){
    
    if (StringUtils.isBlank(email) || StringUtils.isBlank(fileType)) {
      System.err.println("email or filetype is null");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
 
    UserFileType userFileType;
    try{
      userFileType = UserFileType.valueOf(fileType);
    }catch (Exception e) {
      System.err.println("userFileType error: " + fileType);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    
    UserFile file = userFileRepository.findByEmailAndFileType(email, userFileType);
    System.err.println("file found: " + file.getFileName());
    return ResponseEntity.status(HttpStatus.OK).body(file);
  }
  
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public User addUser(String email, String password, Role role, String firstname, String surename,
      String phone, String creditCard, String cvv, String expirationDate, String postalCode,
      String address, String city, String state, String vehicle, String vehicleCategory,
      String plate, String taxiCompany, Boolean babySeat, Boolean wheelChair)
      throws UserServiceException {

    if (StringUtils.isBlank(email) || StringUtils.isBlank(password) || role == null) {
      throw new UserServiceException("Email and Password is mandotary");
    }
    User existinguser = userRepository.findByEmail(email);
    if (existinguser != null) {
      throw new UserServiceException("Email already used");
    }
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setRole(role);
    user.setForename(firstname);
    user.setSurename(surename);
    user.setPhone(phone);
//    user.setPostalCode(postalCode);
//    if (role.compareTo(Role.ROLE_USER) == 0) {
//      user.setCreditCard(creditCard);
//      user.setCvv(cvv);
//      user.setExpirationDate(expirationDate);
//    }
//    if (role.compareTo(Role.ROLE_DRIVER) == 0) {
//      user.setAddressDetail(address);
//      user.setCity(city);
//      user.setState(state);
//      user.setVehicle(vehicle);
//      user.setVehicleCategory(vehicleCategory);
//      user.setPlate(plate);
//      user.setTaxiCompany(taxiCompany);
//      user.setBabySeat(babySeat);
//      user.setWheelChair(wheelChair);
//    }
    return userRepository.save(user);
  }

  @RequestMapping(value = "/login")
  public UserLoginInfoDTO getUserLoginInfo(String email) {
    User user = userRepository.findByEmail(email);
    if(user != null){
      return new UserLoginInfoDTO(user);
    }
    return null;
  }

  @RequestMapping(value = "/forgetPassword")
  public void forgetPassword(String email) {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      String password = RandomStringUtils.random(12, true, true);
      user.setPassword(password);
      userRepository.save(user);
      MimeMessage m = javaMailSender.createMimeMessage();
      try {
        MimeMessageHelper helper = new MimeMessageHelper(m, true);
        helper.setTo(email);
        helper.setSubject("Your New Password");
        helper.setText("Password: " + password);
        javaMailSender.send(m);
      } catch (MessagingException | MailException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
      }
    }
  }
  
  @RequestMapping(value = "/send-verification-code")
  public boolean sendVerificationCode(String email, String code) {
      MimeMessage m = javaMailSender.createMimeMessage();
      try {
        MimeMessageHelper helper = new MimeMessageHelper(m, true);
        helper.setTo(email);
        helper.setSubject("Your Verification Code");
        helper.setText("Verification Code: " + code);
        javaMailSender.send(m);
        return true;
      } catch (MessagingException | MailException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
      }
      return false;
  }
  
  @RequestMapping(value = "/dispute")
  public void dispute(String email, String bidId, String tripId, String subject, String description) {
    Dispute dispute = new Dispute();
    dispute.setEmail(email);
    dispute.setBidId(bidId);
    dispute.setTripId(tripId);
    disputeRepository.save(dispute);
  }
}
