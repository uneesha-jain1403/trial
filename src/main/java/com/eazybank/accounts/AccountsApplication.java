package com.eazybank.accounts;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Accounts Service for EazyBank",
				description = "Create, Read, Update and Delete Operations available for Accounts Service",
				contact = @Contact(
						name = "Sumit K",
						email = "sumit@test.com"
				),
				version = "1.0.0"
		)
)

@SpringBootApplication
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}