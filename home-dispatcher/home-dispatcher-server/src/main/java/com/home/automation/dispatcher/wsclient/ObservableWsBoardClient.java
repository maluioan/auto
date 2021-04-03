package com.home.automation.dispatcher.wsclient;

import com.home.automation.dispatcher.wsclient.frame.observers.FrameObserver;
import com.home.automation.dispatcher.wsclient.frame.observers.WsConnectionObserver;

public interface ObservableWsBoardClient {

    void addFrameObserver(FrameObserver frameObserver);

    void removeFrameObserver(FrameObserver frameObserver);

    void addConnectionObserver(WsConnectionObserver connectionObserver);

    void removeConnectionObserver(WsConnectionObserver connectionObserver);
}
