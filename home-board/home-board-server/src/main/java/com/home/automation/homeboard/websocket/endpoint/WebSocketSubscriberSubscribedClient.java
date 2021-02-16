package com.home.automation.homeboard.websocket.endpoint;

import com.home.automation.homeboard.websocket.message.BoardCommandMessage;

import javax.websocket.Session;

public class WebSocketSubscriberSubscribedClient extends BoardWsSubscribedClient<BoardCommandMessage> {

    @Override
    protected void onOpenInternal(Session session) {
        System.out.println("WebSocketSubscriberSubscribedClient: Opened session: " + session.getId());
    }

    @Override
    protected void onMessageInternal(BoardCommandMessage commandMessage) {
        if (BoardCommandMessage.MessageType.CONNECT.equals(commandMessage.getCommand())) {
            //sen
        }
    }
}
