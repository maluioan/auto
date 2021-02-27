package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.service.HomeBoardService;
import com.home.automation.homeboard.websocket.message.DispatcherMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public class DispatcherWsClient extends BoardWsSubscribedClient<DispatcherMessage> {

    private String dispatcherId;

    @Autowired
    private HomeBoardService homeBoardService;

    @Override
    protected void onOpenInternal(Session session) {
        dispatcherId = super.getHeaderValue("did");
        getSubscriberRegistry().registerDispatcherSubscriber(this);
        logger.info(String.format("Dispatcher with id %s, connected", dispatcherId));
    }

    @Override
    protected void onMessageInternal(DispatcherMessage commandMessage) {
        if (DispatcherMessage.MessageType.CONNECT.name().equals(commandMessage.getCommand())) {
            logger.info(String.format("Received stomp connect message from %s", dispatcherId));

            final DispatcherMessage dispatcherMessage = new DispatcherMessage();
            dispatcherMessage.setCommand(DispatcherMessage.MessageType.CONNECT.name());
            dispatcherMessage.setPayload("CONNECTED");
            super.sendMessage(dispatcherMessage);
            // TODO: treat connect => connected message

        } else if (DispatcherMessage.MessageType.COMMAND.name().equals(commandMessage.getCommand())) {
            final CommandData command = homeBoardService.findCommandById(commandMessage.getCommandId());
            getMessageMediator().handleMicroControllerMessage(commandMessage);
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
        getSubscriberRegistry().removeDispatcherSubscriber(this);
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
