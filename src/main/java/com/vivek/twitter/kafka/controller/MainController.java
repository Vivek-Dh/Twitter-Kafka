package com.vivek.twitter.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vivek.twitter.kafka.producer.TwitterKafkaProducer;

@RestController
public class MainController {
	@Autowired
	TwitterKafkaProducer twitterKafkaProducer;
	@RequestMapping("/start")
	public String start() {
		try {
			twitterKafkaProducer.run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Started";
	}
}
