package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.command.BaseBoardCommand;
import com.home.automation.homeboard.websocket.command.MessageExecutor;
import com.home.automation.homeboard.websocket.message.DispatcherMessage;
import com.home.automation.homeboard.websocket.message.MicroControllerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class BoardMessageMediator implements MessageMediator {

    protected static final Logger logger = LogManager.getLogger(BoardMessageMediator.class);

    private SubscriberRegistry subscriberRegistry;

    private MessageExecutor messageExecutor;

    @Override
    public void handleMicroControllerMessage(final MicroControllerMessage mcMessage) {
        final List<Subscriber> dispatchers = subscriberRegistry.getDispatchers();
        final BaseBoardCommand baseBoardCommand = new BaseBoardCommand(dispatchers, mcMessage.getPayload());
        messageExecutor.execute(baseBoardCommand);
    }

    @Override
    public void handleMicroControllerMessage(final DispatcherMessage commandMessage) {
         // TODO: aici o sa fie o comanda... trebuie sa o luam din DB... executam fiecare actiune de acolo...
        final String commandId = commandMessage.getCommand();
        final Optional<Subscriber> micrControllerByID = subscriberRegistry.getMicrControllerByID(commandId);
        if (micrControllerByID.isPresent()) {
            messageExecutor.execute(null);

        } else {
            logger.warn("No goodd!!!");
        }
    }

    public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    public void setMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
