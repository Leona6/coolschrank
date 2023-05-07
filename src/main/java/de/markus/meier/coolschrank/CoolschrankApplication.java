package de.markus.meier.coolschrank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CoolschrankApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoolschrankApplication.class, args);
	}

}
