package com.home.automation.dispatcher.wsclient.frame.handlers;

import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: implement doar daca e activa trimiterea de maill-uri, si doar daca e o singura poza
public class MailPictureBoardFrameHandler implements BoardFrameHandler {

    protected final Logger logger = LoggerFactory.getLogger(MailPictureBoardFrameHandler.class);

    @Override
    public boolean canHandleFrame(BoardStompMessage stompMessage) {
        return true;
    }

    @Override
    public void handleFrame(BoardStompMessage stompMessage) {
        logger.info("*** Sending mail with picture");
        // TODO: implement
    }
}
