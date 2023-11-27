package fr.uha.ensisa.antidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class AntidemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntidemoApplication.class, args);
	}

}
