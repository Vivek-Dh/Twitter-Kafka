package com.vivek.twitter.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public class TwitterKafkaConsumer {
	@KafkaListener(topics = "dota2",groupId="Group1")
	public void listenWithHeaders(
	  @Payload String message, 
	  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
	      System.out.println(
	        "Received Message: " + message
	        + "from partition: " + partition);
	}
}
