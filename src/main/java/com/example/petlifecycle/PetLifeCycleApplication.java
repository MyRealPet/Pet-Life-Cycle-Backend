package com.example.petlifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.petlifecycle.metadata.config")
public class PetLifeCycleApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetLifeCycleApplication.class, args);
    }
}
