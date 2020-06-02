package edu.whut.cs.jee.mooc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class MoocClassApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(MoocClassApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("All MoocClass: {}");

    }
}
