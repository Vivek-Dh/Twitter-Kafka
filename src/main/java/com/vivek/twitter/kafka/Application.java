package com.vivek.twitter.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;



@SpringBootApplication(scanBasePackages= {"com.vivek.twitter.kafka","com.vivek.twitter.kafka.controller"})
@PropertySource("classpath:/twitter.properties")
public class Application {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}
}
