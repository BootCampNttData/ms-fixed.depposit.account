package com.bootcamp.fixeddeppossitaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FixedDeppossitAccountApplication {

	public static void main(String[] args) {
		System.setProperty("jdk.tls.client.protocols","TLSv1.2");
		SpringApplication.run(FixedDeppossitAccountApplication.class, args);
	}

}
