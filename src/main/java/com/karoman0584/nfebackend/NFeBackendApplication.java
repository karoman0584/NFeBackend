package com.karoman0584.nfebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NFeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NFeBackendApplication.class, args);
    }

}
