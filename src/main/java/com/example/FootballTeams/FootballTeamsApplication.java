package com.example.FootballTeams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FootballTeamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballTeamsApplication.class, args);
	}

}
