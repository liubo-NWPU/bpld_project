package com.gis.trans.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Producer {
 @Autowired
 private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMessage(String destinationName,final String message){

    jmsMessagingTemplate.convertAndSend(destinationName,message);
    }
}
