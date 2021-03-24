package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.command.BaseBoardCommand;
import com.home.automation.homeboard.websocket.command.MessageExecutor;
import com.home.automation.homeboard.websocket.message.request.BoardRequestMessage;
import com.home.automation.homeboard.websocket.message.request.CommandRequestMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class BoardMessageMediator implements MessageMediator {

    protected static final Logger logger = LogManager.getLogger(BoardMessageMediator.class);

    private SubscriberRegistry subscriberRegistry;

    private MessageExecutor messageExecutor;

    @Override
    public void handleDispatchMessage(final BoardRequestMessage dispMessage) {
        BaseBoardCommand baseBoardCommand = null;

        if (dispMessage instanceof CommandRequestMessage) {
           final List<Subscriber> dispatchers = subscriberRegistry.getDispatchersSubscribers();
           baseBoardCommand = new BaseBoardCommand(dispatchers, Arrays.asList(dispMessage));

        } else {
            // TODO: get command, parse command into multiple boards
           final Optional<Subscriber> dispatchers = subscriberRegistry.getMicrControllerByID("");
           baseBoardCommand = new BaseBoardCommand(Arrays.asList(dispatchers.get()), Arrays.asList(dispMessage));
        }

        messageExecutor.execute(baseBoardCommand);
    }

    public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    public void setMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
