package com.ntoutakeout.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class NtouTakeoutBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NtouTakeoutBackendApplication.class, args);
    }

}
