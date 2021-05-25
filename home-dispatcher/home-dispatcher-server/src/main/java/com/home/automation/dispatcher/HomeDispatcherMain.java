package com.home.automation.dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.home.automation.dispatcher.domain")
@ImportResource("classpath:/config/home-dispatcher-config.xml")
public class HomeDispatcherMain {

    public static void main(String[] args) {
        SpringApplication.run(HomeDispatcherMain.class, args);
    }
}
