package com.home.automation.dispatcher.wsclient.frame.handlers;


import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;

// pornesti simplu, daca nu functioneaza aplici incetul cu incetul chestiile care stii ca functioneaza
public interface BoardFrameHandler {

    /**
     *
     * @param stompMessage
     * @return
     */
    boolean canHandleFrame(BoardStompMessage stompMessage);

    /**
     *
     * @param stompMessage
     */

    void handleFrame(BoardStompMessage stompMessage);
}
