package com.chatApp.chatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
	}

	//what how the token work... https://connect2id.com/products/nimbus-jose-jwt => authorize token (Add Role into JWT) => authentication
}
