package com.chatApp.chatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
	}

	//encode, decode password => authorize token => authentication
}
