package com.home.automation.homeboard.websocket.command;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.message.BoardRequestMessage;

import java.util.List;

public class BaseBoardCommand {

    private List<Subscriber> subscribers;

    private BoardRequestMessage message;

    public BaseBoardCommand(final List<Subscriber> sub, final BoardRequestMessage msg) {
        this.message = msg;

        this.subscribers = sub;
    }

    public void execute() {
        subscribers.forEach(this::sendMessageToSubscriber);
    }

    private void sendMessageToSubscriber(Subscriber subscriber) {
        subscriber.sendMessage(message);
    }
}
