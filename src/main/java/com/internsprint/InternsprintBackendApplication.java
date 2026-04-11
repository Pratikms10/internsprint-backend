package com.internsprint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InternsprintBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(InternsprintBackendApplication.class, args);
    }
}