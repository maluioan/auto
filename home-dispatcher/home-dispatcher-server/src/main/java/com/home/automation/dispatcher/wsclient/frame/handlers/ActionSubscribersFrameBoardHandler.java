package com.home.automation.dispatcher.wsclient.frame.handlers;

import com.home.automation.dispatcher.messages.FeedbackMessageData;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.dispatcher.wsclient.messages.MessageDispatcherService;
import com.home.automation.homeboard.ws.DefaultMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: next in line, maine!!!
public class ActionSubscribersFrameBoardHandler implements BoardFrameHandler {

    @Autowired
    private MessageDispatcherService messageDispatcherService;

    @Override
    public boolean canHandleFrame(BoardStompMessage stompMessage) {
        // TODO: add check for public message, daca nu trebuie sa ajunga la toti subscriberi, intoarce false
        return stompMessage.getPayload() instanceof DefaultMessagePayload;
    }

    @Override
    public void handleFrame(final BoardStompMessage stompMessage) {
        FeedbackMessageData feedbackMessageData;
        final DefaultMessagePayload payload = (DefaultMessagePayload)stompMessage.getPayload();
        messageDispatcherService.sendFeedbackMessageToSubscribers(payload, payload.getExecutorId());
    }
}
