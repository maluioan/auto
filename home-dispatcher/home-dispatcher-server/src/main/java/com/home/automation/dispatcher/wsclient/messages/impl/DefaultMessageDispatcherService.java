package com.home.automation.dispatcher.wsclient.messages.impl;

import com.home.automation.dispatcher.wsclient.WsBoardClient;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.dispatcher.wsclient.messages.MessageDispatcherService;
import com.home.automation.homeboard.ws.WSMessagePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultMessageDispatcherService implements MessageDispatcherService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageDispatcherService.class);

    // TODO: put this in a properties file
    private static final String FEEDBACK_ACTIONS = "/dispatcher-broker/topic/feedback/action/%s";
    private static final String FEEDBACK_USER = "/dispatcher-broker/topic/feedback/user/%s";


    @Resource(name = "wsBoardClient")
    private WsBoardClient wsBoardClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessageToBoard(final WSMessagePayload payload, String messageId) {
        if (wsBoardClient.isConnected()) {
            wsBoardClient.sendMessage(wrapToBoardStompMessage(payload, messageId));
        } else {
            logger.warn("Not connected to board server");
        }
    }

    @Override
    public void sendFeedbackMessageToUser(final Object payload, String userName) {
        final String destination = String.format(FEEDBACK_USER, userName);
        simpMessagingTemplate.convertAndSend(destination, payload);
    }

    @Override
    public void sendFeedbackMessageToSubscribers(final Object payload, final String actionId) {
        final String destination = String.format(FEEDBACK_ACTIONS, actionId);
        simpMessagingTemplate.convertAndSend(destination, payload);
    }

    private BoardStompMessage wrapToBoardStompMessage(WSMessagePayload payload, String messageId) {
        final BoardStompMessage bsm = new BoardStompMessage();
        bsm.setPayload(payload);
        bsm.setMessageId(messageId);
        return bsm;
    }
}
