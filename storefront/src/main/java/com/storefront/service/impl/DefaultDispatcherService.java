package com.storefront.service.impl;

import com.home.automation.dispatcher.client.DispatcherClient;
import com.storefront.service.DispatcherService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultDispatcherService implements DispatcherService {

    protected final Logger logger = LogManager.getLogger(DefaultDispatcherService.class);

    @Autowired
    private DispatcherClient dispatcherClient;

    @Override
    public String getDispatcherToken(final String userName) {
        // TODO: treat exceptions
        // TODO: seton session
        // TODO: finish
        String result = null;
        try {
            result = dispatcherClient.getDispatcherToken(userName).getBody();
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

}
