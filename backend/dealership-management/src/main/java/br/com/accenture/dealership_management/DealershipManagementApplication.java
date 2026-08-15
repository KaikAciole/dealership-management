package br.com.accenture.dealership_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DealershipManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealershipManagementApplication.class, args);
	}

}
