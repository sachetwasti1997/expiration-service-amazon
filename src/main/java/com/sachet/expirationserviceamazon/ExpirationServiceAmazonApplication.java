package com.sachet.expirationserviceamazon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class ExpirationServiceAmazonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpirationServiceAmazonApplication.class, args);
	}

}
