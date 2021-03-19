package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.service.HomeBoardService;
import com.home.automation.homeboard.websocket.message.ReceivedCommandMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public class CommandReceiverWsClient extends AbstractWsClient<ReceivedCommandMessage> {

    private String dispatcherId;

    @Autowired
    private HomeBoardService homeBoardService;

    @Override
    protected void onOpenInternal(Session session) {
        dispatcherId = super.getHeaderValue("subscriberID");
        getSubscriberRegistry().registerDispatcherSubscriber(this);
        logger.info(String.format("Dispatcher with id %s, connected", dispatcherId));
    }

    @Override
    protected void onMessageInternal(ReceivedCommandMessage commandMessage) {
        if (ReceivedCommandMessage.MessageType.CONNECT.equals(commandMessage.getCommand())) {
            logger.info(String.format("Received stomp connect message from %s", dispatcherId));

            final ReceivedCommandMessage receivedCommandMessage = new ReceivedCommandMessage();
            receivedCommandMessage.setCommand(ReceivedCommandMessage.MessageType.CONNECTED);
            receivedCommandMessage.setOriginalPayload(commandMessage.getOriginalPayload());
            receivedCommandMessage.setHeaders(commandMessage.getHeaders());

            super.sendMessage(receivedCommandMessage);

        } else if (ReceivedCommandMessage.MessageType.SEND.equals(commandMessage.getCommand())) {
//            final CommandData command = homeBoardService.findCommandById(commandMessage.getComma7ndId());
            final ReceivedCommandMessage rec = new ReceivedCommandMessage();
            rec.setCommand(ReceivedCommandMessage.MessageType.MESSAGE);
            rec.setOriginalPayload(commandMessage.getOriginalPayload()); // TODO: add sucscription header
            rec.setHeaders(commandMessage.getHeaders());


            getMessageMediator().handleDispatchMessage(rec);
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
        getSubscriberRegistry().removeDispatcherSubscriber(dispatcherId);
    }

    @Override
    public String getIdentifier() {
        return dispatcherId;
    }

    @Override
    public Type getType() {
        return Type.DISPATCHER;
    }
}
