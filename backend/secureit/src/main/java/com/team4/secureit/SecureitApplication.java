package com.team4.secureit;

import com.team4.secureit.config.AppProperties;
import com.team4.secureit.service.KeyStoreService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties(AppProperties.class)
public class SecureitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureitApplication.class, args);
	}

	@Bean
	public CommandLineRunner myCommandLineRunner(KeyStoreService keyStoreService) {
		return args -> {
			keyStoreService.init();
		};
	}
}
