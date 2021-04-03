package com.home.automation.homeboard.websocket.command;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import org.springframework.util.Assert;

import java.util.List;

public class BaseBoardCommand {

    private final List<Subscriber> subscribers;

    private final WSRequest message;

    public BaseBoardCommand(final List<Subscriber> sub, final WSRequest message) {
        Assert.notEmpty(sub, "Need subscribers");
        Assert.notNull(message, "Need messages");

        this.message = message;
        this.subscribers = sub;
    }

    public void execute() {
        subscribers.forEach(this::sendMessageToSubscriber);
    }

    private void sendMessageToSubscriber(final Subscriber subscriber) {
        subscriber.sendMessage(message);
    }
}
