package com.home.automation.homeboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.home.automation.homeboard")
@EntityScan(basePackages = "com.home.automation.homeboard")
@ImportResource("classpath:/config/home-board-config.xml")
public class HomeBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBoardApplication.class, args);
    }

}
