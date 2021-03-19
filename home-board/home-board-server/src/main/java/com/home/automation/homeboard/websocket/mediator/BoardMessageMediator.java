package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.command.BaseBoardCommand;
import com.home.automation.homeboard.websocket.command.MessageExecutor;
import com.home.automation.homeboard.websocket.message.BoardRequestMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BoardMessageMediator implements MessageMediator {

    protected static final Logger logger = LogManager.getLogger(BoardMessageMediator.class);

    private SubscriberRegistry subscriberRegistry;

    private MessageExecutor messageExecutor;

    @Override
    public void handleDispatchMessage(final BoardRequestMessage mcMessage) {
        final List<Subscriber> dispatchers = subscriberRegistry.getDispatchersSubscribers();
        final BaseBoardCommand baseBoardCommand = new BaseBoardCommand(dispatchers, mcMessage);
        messageExecutor.execute(baseBoardCommand);
    }

//    @Override
//    public void handleDispatchMessage(final BoardRequestMessage commandMessage) {
         // TODO: aici o sa fie o comanda... trebuie sa o luam din DB... executam fiecare actiune de acolo...
        // TODO: implement this
//        final Long commandId = commandMessage.getCommandId();
//        final Optional<Subscriber> micrControllerByID = subscriberRegistry.getMicrControllerByID(commandId);
//        if (micrControllerByID.isPresent()) {
//            messageExecutor.execute(null);
//
//        } else {
//            logger.warn("No goodd!!!");
//        }
//        messageExecutor.execute(commandMessage);
//    }

    public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    public void setMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
