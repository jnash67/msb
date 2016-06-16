package com.medcognize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
// Enable additional servlet filters for wscdn and cloud hosted fontawesome
@ServletComponentScan({"com.vaadin.wscdn", "org.peimari.dawn"})
@Slf4j
public class MedcognizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedcognizeApplication.class);
    }

    @Bean
    public CommandLineRunner loadData(UserService repo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                String profile = System.getProperty("spring.profiles.active");
                log.warn("spring.profiles.active --> " + profile + " (if null then profile is default)");
            }
        };
    }
}
