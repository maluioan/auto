package com.home.automation.dispatcher.service.impl;

import com.home.automation.dispatcher.service.DispatcherService;
import com.home.automation.dispatcher.wsclient.WsBoardClient;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.homeboard.ws.CommandMessagePayload;
import com.home.automation.homeboard.ws.WSMessagePayload;
import com.home.automation.util.CommonUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WsBoardDispatcherManager implements DispatcherService {

    @Resource(name = "wsObservedBoardClient")
    private WsBoardClient wsBoardClient;

    @Override
    public void sendMessageWithFeedback(String commandId, String userName) {
        final BoardStompMessage stompMessage = new BoardStompMessage();
        stompMessage.setMessageId(CommonUtils.createRandomSixDigitsId());
        stompMessage.setPayload(createCommandMessage(commandId, userName));

        wsBoardClient.sendMessage(stompMessage);
    }

    private CommandMessagePayload createCommandMessage(final String commandId, final String userName) {
        final CommandMessagePayload owm = new CommandMessagePayload();
        owm.setCommandId(commandId);
        owm.setUserName(userName);
        return owm;
    }
}
