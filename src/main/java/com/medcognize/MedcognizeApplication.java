package com.medcognize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// for the security aspects see https://vaadin.com/blog/-/blogs/filter-based-spring-security-in-vaadin-applications
// and https://github.com/peholmst/SpringSecurityDemo
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "domain")
@EntityScan(basePackages = "domain")
// Enable additional servlet filters for wscdn and cloud hosted fontawesome
@ServletComponentScan({"com.vaadin.wscdn", "org.peimari.dawn"})
//@ComponentScan(basePackageClasses = UserService.class)
@Slf4j
public class MedcognizeApplication {

    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

        @Autowired
        UserService repo;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            //@formatter:off
            auth.getDefaultUserDetailsService()
            auth.userDetailsService(repo).passwordEncoder(new BCryptPasswordEncoder());
            //@formatter:on
        }

        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return authenticationManager();
        }

        static {
            // Use a custom SecurityContextHolderStrategy
            SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MedcognizeApplication.class);
    }

    @Bean
    public CommandLineRunner loadData() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                String profile = System.getProperty("spring.profiles.active");
                log.warn("spring.profiles.active --> " + profile + " (if null then profile is default)");
            }
        };
    }
}
