package com.farebid.bidservice;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class FareBidAmqbListener implements Runnable {

  private RabbitTemplate rabbitTemplate;
  private Queue q;
  private boolean run = true;
  private AmqpAdmin amqpAdmin;
  private ExecutorService executor;
  
  public FareBidAmqbListener(RabbitTemplate rabbitTemplate, String queueName, 
      AmqpAdmin amqpAdmin, ExecutorService executor) {
    this.rabbitTemplate = rabbitTemplate;
    this.amqpAdmin = amqpAdmin;
    this.executor = executor;
    q = new Queue(queueName);
    amqpAdmin.declareQueue(q);
  }

  @Override
  public void run() {
     while (!Thread.interrupted() && run) {
      try{
        Message m = rabbitTemplate.receive(q.getName(), 10000);
        if (m != null) {
          System.err.println(q.getName() + " " + new Date() + ": " + new String(m.getBody()));
        } else {
          System.err.println("nothing in " + q.getName());
        }
      }catch (Exception e) {
        // TODO
      }
    }
  }

  public void exit() {
    amqpAdmin.deleteQueue(q.getName());
    run = false;
  }
  
  public void start(){
    executor.execute(this);
  }
  
  public String getQueueName(){
    return q.getName();
  }
}
