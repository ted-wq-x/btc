package com.go2going;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtcApplication.class, args);
	}

}
