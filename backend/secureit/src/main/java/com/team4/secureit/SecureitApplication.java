package com.team4.secureit;

import com.team4.secureit.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
public class SecureitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureitApplication.class, args);
	}

}
