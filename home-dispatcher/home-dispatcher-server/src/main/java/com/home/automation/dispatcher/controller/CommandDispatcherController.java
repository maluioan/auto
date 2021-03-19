package com.home.automation.dispatcher.controller;

import com.home.automation.dispatcher.messages.CommandMessageClient;
import com.home.automation.dispatcher.service.DispatcherService;
import com.home.automation.dispatcher.wsclient.messages.OutgoingWsMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.home.automation.util.CommonUtils.getCurrentDate;

//import static org.apache.tomcat.util.http.FastHttpDateFormat.getCurrentDate; // TODO: test

@Controller
public class CommandDispatcherController {

    @Autowired
    private DispatcherService wsBoardDispatcherManager;

    @MessageMapping("/client/message")
//    @SendTo("/command/board")
//    @SendToUser()
    public String saveAndBroadcastCommand(@Payload CommandMessageClient message, Principal principal) {
        if (StringUtils.isNoneBlank(message.getCommandId())) {
            final String commandId = message.getCommandId();
            final String userName = principal.getName();
            wsBoardDispatcherManager.sendMessageWithFeedback(commandId, userName);
        }
        return "received: " + message;
    }

}
