package com.medcognize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class MedcognizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedcognizeApplication.class);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository repo) {
        return (args) -> {
            String profile = System.getProperty("spring.profiles.active");
            log.warn("spring.profiles.active --> " + profile + " (if null then profile is default)");
        };
    }

}
