package com.vivek.twitter.kafka.producer;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;


@Component
@PropertySource("classpath:/twitter.properties")
public class TwitterKafkaProducer {
    private static final String topic = "dota2";
    @Value("${consumerkey}")
    private String consumerKey;
    @Value("${consumersecret}")
    private String consumerSecret;
    @Value("${accesstoken}")
    private String accessToken;
    @Value("${accesstokensecret}")
    private String accessTokenSecret;
    
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    
    public void run() throws InterruptedException {
        System.out.println(consumerKey+"**"+accessToken);
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        endpoint.trackTerms(Lists.newArrayList("dota2",
                "gaben"));
        Authentication auth = new OAuth1(consumerKey, consumerSecret, accessToken,
                accessTokenSecret);
        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();
        client.connect();
        for (int msgRead = 0; msgRead < 5; msgRead++) {
            String message = null;
            try {
                message = queue.take();
                System.out.println(message.substring(0, 25));
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("Stream ended");
            }
            kafkaTemplate.send(topic,message);
        }
        client.stop();
    }
    
    @KafkaListener(topics = "dota2",groupId="Group1")
	public void listenWithHeaders(
	  @Payload String message, 
	  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
	      System.out.println(
	        "Received Message: " + message
	        + "from partition: " + partition);
	}
    
   
}
