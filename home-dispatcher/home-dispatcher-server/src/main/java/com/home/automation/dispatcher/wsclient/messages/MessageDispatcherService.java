package com.home.automation.dispatcher.wsclient.messages;

import com.home.automation.homeboard.ws.WSMessagePayload;

public interface MessageDispatcherService {

    void sendMessageToBoard(WSMessagePayload payload, String messageId);

    void sendFeedbackMessageToUser(Object payload, String userName);

    void sendFeedbackMessageToSubscribers(final Object payload, final String actionId);
}
