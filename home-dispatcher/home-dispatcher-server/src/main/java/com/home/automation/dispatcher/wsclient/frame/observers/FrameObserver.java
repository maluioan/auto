package com.home.automation.dispatcher.wsclient.frame.observers;


import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;

// pornesti simplu, daca nu functioneaza aplici incetul cu incetul chestiile care stii ca functioneaza
public interface FrameObserver {
    void handleFrame(BoardStompMessage stompMessage);
}
