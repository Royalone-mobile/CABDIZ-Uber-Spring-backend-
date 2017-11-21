package com.farebid.messagingservice;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.farebid.messagingservice.mongo.FareBidMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.http.client.IFcmClient;
import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import de.bytefish.fcmjava.responses.FcmMessageResponse;
import de.bytefish.fcmjava.responses.FcmMessageResultItem;

@Service
public class RedisMessageSubscriber implements MessageListener {

  @Autowired
  ObjectMapper objectMapper;
  
  @Autowired
  FareBidMessageRepository fareBidMessageRepository;
  
  public void onMessage(Message message, byte[] pattern) {

    IFcmClient client = null;
    FareBidMessage fareBidMessage = null;
    try {
      System.out.println("Message received: " + message.toString());

      fareBidMessage =objectMapper.readValue(new String(message.getBody()), FareBidMessage.class);
      
      // Construct the FCM Client Settings with your API Key:
      IFcmClientSettings clientSettings = new FixedFcmClientSettings("your_api_key_here");

      // Instantiate the FcmClient with the API Key:
      client = new FcmClient(clientSettings);

      FcmMessageOptions options =
          FcmMessageOptions.builder().setTimeToLive(Duration.ofHours(1)).build();

      // TODO
      NotificationPayload payload = NotificationPayload.builder().setBody("New Message")
          .setTitle("Test").setTag("test").build();

      Map<String, Object> data = new HashMap<>();
      data.put("text", fareBidMessage.getText());

      // Send a message
      // TODO
      DataUnicastMessage mes = new DataUnicastMessage(options, fareBidMessage.getToken(), data, payload);
      fareBidMessage.setSuccessfull(true);
      FcmMessageResponse response = client.send(mes);
      for (FcmMessageResultItem result : response.getResults()) {
        if (result.getErrorCode() != null) {
          System.out.printf("Sending failed. Error Code %s\n", result.getErrorCode());
          fareBidMessage.setSuccessfull(false);
        }
      }
    } catch (Exception e) {
      //TODO log
      e.printStackTrace();
      fareBidMessage.setSuccessfull(false);
    } finally {
      try {
        fareBidMessageRepository.save(fareBidMessage);
        client.close();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
