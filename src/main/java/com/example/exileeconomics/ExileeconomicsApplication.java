package com.example.exileeconomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExileeconomicsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExileeconomicsApplication.class, args);
    }
}
