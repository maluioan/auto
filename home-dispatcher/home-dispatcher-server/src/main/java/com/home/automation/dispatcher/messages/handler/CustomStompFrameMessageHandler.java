package com.home.automation.dispatcher.messages.handler;

import com.home.automation.dispatcher.messages.handler.frame.FrameHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

public class CustomStompFrameMessageHandler implements MessageHandler, SmartLifecycle {

    private final Object lifecycleMonitor = new Object();

    private final SubscribableChannel clientInboundChannel;

    private final MultiValueMap<SimpMessageType, FrameHandler> subscriptionHandlers = new LinkedMultiValueMap<>();

    private volatile boolean isRunnning;

    public CustomStompFrameMessageHandler(SubscribableChannel clientInboundChannel) {
        this.clientInboundChannel = clientInboundChannel;
    }

    @Override
    public void start() {
        System.out.println("custom start msg handler");
        synchronized (this.lifecycleMonitor) {
            this.clientInboundChannel.subscribe(this);
            this.isRunnning = true;
        }
    }

    @Override
    public void stop() {
        System.out.println("custom stop msg handler");
        synchronized (this.lifecycleMonitor) {
            this.clientInboundChannel.unsubscribe(this);
            this.isRunnning = false;
        }
    }

    @Override
    public boolean isRunning() {
        return isRunnning;
    }

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        final MessageHeaders headers = message.getHeaders();
        final SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);

        final List<FrameHandler> frameHandlers = this.subscriptionHandlers.get(messageType);
        CollectionUtils.emptyIfNull(frameHandlers).stream()
                .filter(frameHandler -> frameHandler.canHandleFrame(message))
                .forEach(frameHandler -> frameHandler.handleFrame(message));

    }

    public void addSubscriptionHandler(final FrameHandler frameHandler) {
        this.subscriptionHandlers.add(SimpMessageType.SUBSCRIBE, frameHandler);
    }

    public void addConnectHandler(final FrameHandler frameHandler) {
        this.subscriptionHandlers.add(SimpMessageType.CONNECT, frameHandler);
    }

    public void addMessageHandler(final FrameHandler frameHandler) {
        this.subscriptionHandlers.add(SimpMessageType.MESSAGE, frameHandler);
    }
}
