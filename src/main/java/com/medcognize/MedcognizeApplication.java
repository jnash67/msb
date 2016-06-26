package com.medcognize;

import com.medcognize.domain.basic.EmailAddress;
import com.medcognize.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@ComponentScan(basePackageClasses = UserDetailsServiceImpl.class)
@EnableJpaRepositories
// Enable additional servlet filters for wscdn and cloud hosted fontawesome
@ServletComponentScan({"com.vaadin.wscdn", "org.peimari.dawn"})
@Slf4j
public class MedcognizeApplication {

    @Autowired
    UserRepository repo;

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
                dbChecks();
            }
        };
    }

    public void dbChecks() {
        boolean adminExists = repo.existsByAdminTrue();
        System.out.println("existsByAdminTrue --> " + adminExists);
        long numAdmins = repo.countByAdminTrue();
        System.out.println("countByAdminTrue --> " + numAdmins);
        boolean existsByUserName = repo.existsByUsername("admin@admin.com");
        System.out.println("existsByUserName --> " + existsByUserName);
        if (!adminExists) {
            try {
                EmailAddress ae = new EmailAddress("admin@admin.com");
                String pass = "Passwor4";
                if (!UserUtil.existsByUsername(repo, ae)) {
                    UserUtil.createNewAdminUser(repo, ae, pass);
                    adminExists = repo.existsByAdminTrue();
                    System.out.println("existsByAdminTrue --> " + adminExists);
                    numAdmins = repo.countByAdminTrue();
                    System.out.println("countByAdminTrue --> " + numAdmins);
                } else {
                    log.error("Default admin email/username exists as non-admin user!");
                }
            } catch (Exception e) {
                log.error("Error adding default admin user!", e);
                e.printStackTrace();
            }
        }
    }
}
