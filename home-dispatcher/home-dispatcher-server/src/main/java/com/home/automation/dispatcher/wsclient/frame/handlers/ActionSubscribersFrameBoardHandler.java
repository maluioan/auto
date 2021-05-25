package com.home.automation.dispatcher.wsclient.frame.handlers;

import com.home.automation.dispatcher.service.domain.BoardMessageService;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.homeboard.ws.NonSubscribedBoardMessagePayload;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.util.List;

// TODO: next in line, maine!!!
public class ActionSubscribersFrameBoardHandler implements BoardFrameHandler {

    private final String destination;

    @Autowired
    private BoardMessageService boardMessageService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ActionSubscribersFrameBoardHandler(String destination) {
        this.destination = destination;
    }

    @Override
    public boolean canHandleFrame(BoardStompMessage stompMessage) {
        // TODO: add check for public message, daca nu trebuie sa ajunga la toti subscriberi, intoarce false
        return !(stompMessage.getPayload() instanceof NonSubscribedBoardMessagePayload);
    }

    @Override
    public void handleFrame(final BoardStompMessage stompMessage) {
//        stompMessage.getHeaders()
//        boardMessageService.getBoardMessageByMessageId(stompMessage.getHeaders().getMessageId()); // TODO:
        final List<String> actionId = stompMessage.getHeaders().get("actionId");
        if (CollectionUtils.isNotEmpty(actionId)) {
            simpMessagingTemplate.convertAndSend(destination + "/" + actionId.get(0), "Board:" + stompMessage.getPayload());
        }
    }
}
