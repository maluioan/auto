package com.home.automation.dispatcher.service.impl;

import com.home.automation.dispatcher.messages.CommandMessageClient;
import com.home.automation.dispatcher.service.DispatcherService;
import com.home.automation.dispatcher.wsclient.WsBoardClient;
import com.home.automation.dispatcher.wsclient.messages.OutgoingWsMessage;
import com.home.automation.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

import static com.home.automation.util.CommonUtils.getCurrentDate;

@Component
public class WsBoardDispatcherManager implements DispatcherService {

    @Autowired
    private WsBoardClient wsBoardClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessageWithFeedback(String commandId, String userName) {
        wsBoardClient.sendMessage(createOutgoingWsMessage(commandId, userName));
    }

    private OutgoingWsMessage createOutgoingWsMessage(final String commandId, final String userName) {
        final OutgoingWsMessage owm = new OutgoingWsMessage();
        owm.setCommandId(commandId);
        owm.setUser(userName);
        owm.setCreatedDate(getCurrentDate());
        owm.setExecutionId(CommonUtils.createRandomSixDigitsId());
        return owm;
    }

}

