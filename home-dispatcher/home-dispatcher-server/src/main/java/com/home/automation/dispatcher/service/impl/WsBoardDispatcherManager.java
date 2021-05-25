package com.home.automation.dispatcher.service.impl;

import com.home.automation.dispatcher.service.DispatcherService;
import com.home.automation.dispatcher.wsclient.WsBoardClient;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.homeboard.ws.ActionMessagePayload;
import com.home.automation.util.CommonUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Deprecated
@Component
public class WsBoardDispatcherManager implements DispatcherService {

    @Resource(name = "wsBoardClient")
    private WsBoardClient wsBoardClient;

    @Override // TODO: add feedback intr-o forma sau alta
    public void sendMessageWithFeedback(String actionId, String userName, Object payload) {
        final BoardStompMessage stompMessage = new BoardStompMessage();
        stompMessage.setMessageId(CommonUtils.createRandomSixDigitsId());
        stompMessage.setPayload(createCommandMessage(actionId, userName, payload));

        wsBoardClient.sendMessage(stompMessage);
    }

    private ActionMessagePayload createCommandMessage(final String actionId, final String userName, final Object payload) {
        final ActionMessagePayload owm = new ActionMessagePayload();
        owm.setMessageId(actionId);
        owm.setExecutorId(userName);
        owm.setPayload(payload);
        return owm;
    }
}
