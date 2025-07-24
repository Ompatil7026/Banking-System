package com.banking.Banking_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankingSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankingSystemApplication.class, args);
	}

}
