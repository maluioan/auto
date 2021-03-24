package com.home.automation.homeboard.websocket.command;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.message.request.BoardRequestMessage;
import org.springframework.util.Assert;

import java.util.List;

public class BaseBoardCommand {

    private final List<Subscriber> subscribers;

    private final List<BoardRequestMessage> messages;

    public BaseBoardCommand(final List<Subscriber> sub, final List<BoardRequestMessage> messages) {
        Assert.notEmpty(sub, "Need subscribers");
        Assert.notEmpty(messages, "Need messages");

        this.messages = messages;
        this.subscribers = sub;
    }

    public void execute() {
        subscribers.forEach(this::sendMessageToSubscriber);
    }

    private void sendMessageToSubscriber(Subscriber subscriber) {
        messages.forEach(subscriber::sendMessage);
    }
}
