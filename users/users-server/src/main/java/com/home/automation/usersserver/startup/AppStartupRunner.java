package com.home.automation.usersserver.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Resource
    private AppStartupStrategy appStartupStrategy;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("* Boot's startup runner initialized!!!");
        appStartupStrategy.execute();
    }
}
