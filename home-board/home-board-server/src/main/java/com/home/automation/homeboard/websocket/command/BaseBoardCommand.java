package com.home.automation.homeboard.websocket.command;

import com.home.automation.homeboard.websocket.Subscriber;

import java.util.List;

public class BaseBoardCommand {

    private List<Subscriber> subscribers;

    private Object message;

    public BaseBoardCommand(final List<Subscriber> sub, final Object msg) {
        this.message = msg;
        this.subscribers = sub;
    }

    public void execute() {
        subscribers.forEach(sub -> sub.sendMessage(message));
    }
}
