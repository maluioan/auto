package com.home.automation.dispatcher.messages.handler.frame;

import org.springframework.messaging.Message;

public interface FrameHandler {

    boolean canHandleFrame(Message<?> message);

    void handleFrame(Message<?> message);
}
