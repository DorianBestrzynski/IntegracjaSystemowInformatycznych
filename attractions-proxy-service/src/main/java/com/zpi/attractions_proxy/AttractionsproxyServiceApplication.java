package com.zpi.attractions_proxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class AttractionsproxyServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AttractionsproxyServiceApplication.class, args);
	}
}
