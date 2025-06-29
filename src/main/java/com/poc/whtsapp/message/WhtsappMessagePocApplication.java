package com.poc.whtsapp.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WhtsappMessagePocApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhtsappMessagePocApplication.class, args);
	}

}
