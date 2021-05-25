package com.home.automation.dispatcher.controller;

import com.home.automation.dispatcher.messages.ActionMessage;
import com.home.automation.dispatcher.service.BoardMessageHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

//import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate; // TODO: test

@Controller
public class DispatcherActionController {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherActionController.class);

    @Autowired
    private BoardMessageHandlerService boardMessageHandlerService;

    @MessageMapping("/client/message")
    @SendToUser("/action") // TODO:fara asta, 'principal' e null
    public void saveAndBroadcastCommand(@Payload ActionMessage message, Principal principal) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received client message: " + message.getActionId());
        }
        boardMessageHandlerService.storeAndExecuteMessage(message, principal.getName());
    }

    @SubscribeMapping("/feedback")
    public String getFeedbacks() {
        return "hello feedback";
    }
}
