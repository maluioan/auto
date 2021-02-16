package com.home.automation.dispatcher.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CommandDispatcherController {

    @MessageMapping("/client/message")
    @SendTo("/command/board")
    public String saveAndBroadcastCommand(@Payload String message) {
        return "received: " + message;
    }
}
