package com.services.investment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@org.springframework.cloud.openfeign.EnableFeignClients
@SpringBootApplication
public class InvestmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentServiceApplication.class, args);
	}

}
