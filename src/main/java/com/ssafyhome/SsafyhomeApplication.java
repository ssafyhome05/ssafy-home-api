package com.ssafyhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SsafyhomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsafyhomeApplication.class, args);
	}

}
