package com.home.automation.dispatcher.wsclient;

import com.home.automation.dispatcher.wsclient.frame.observers.FrameObserver;
import com.home.automation.dispatcher.wsclient.frame.observers.WsConnectionObserver;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;

import java.util.List;

// TODO: not sure what I wanted to do here, remove or finish
public class DelegatingObservableWsBoardClient implements ObservableWsBoardClient, WsBoardClient {

    private List<FrameObserver> frameObservers;
    private List<WsConnectionObserver> connectionObservers;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final WsBoardClient delegate;

    public DelegatingObservableWsBoardClient(final WsBoardClient delegate) {
        this.delegate = delegate;
        this.delegate.addFrameObserver(this::handleFrame);
    }

    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    @Override
    public void connect() {
        // before connection
        delegate.connect();
        // after connection
    }

    @Override
    public void disconnect() {
        // before disconnect
        this.delegate.disconnect();
        // after disconnect
    }

    @Override
    public boolean sendMessage(BoardStompMessage msg) {
        return delegate.sendMessage(msg);
    }

    private void handleFrame(BoardStompMessage stompMessage) {
        System.out.println("some observed message was received");
        simpMessagingTemplate.send("/command/board", new GenericMessage<>(stompMessage.getPayload()));
        CollectionUtils.emptyIfNull(frameObservers).stream()
                .forEach(fo -> fo.handleFrame(stompMessage));
    }

    //
    @Override
    public void addFrameObserver(FrameObserver frameObserver) {

    }

    @Override
    public void removeFrameObserver(FrameObserver frameObserver) {

    }

    @Override
    public void addConnectionObserver(WsConnectionObserver connectionObserver) {

    }

    @Override
    public void removeConnectionObserver(WsConnectionObserver connectionObserver) {

    }
}
