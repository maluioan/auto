package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.command.BaseBoardCommand;
import com.home.automation.homeboard.websocket.command.MessageExecutor;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


final class BoardMessageMediator implements MessageMediator {

    protected static final Logger logger = LogManager.getLogger(BoardMessageMediator.class);

    private SubscriberRegistry subscriberRegistry;

    private MessageExecutor messageExecutor;

    BoardMessageMediator() {}

    @Override
    public void handleDispatchMessage(final StompRequest dispMessage) {
        final List<Subscriber> dispatchers = subscriberRegistry.getDispatchersSubscribers();
        if (CollectionUtils.isNotEmpty(dispatchers))
        {
            final BaseBoardCommand baseBoardCommand = new BaseBoardCommand(dispatchers, dispMessage);
            messageExecutor.execute(baseBoardCommand);
        }
    }

    @Override
    public void handleBoardMessages(final PioRequest pioRequest) {
        final Optional<Subscriber> dispatchers = subscriberRegistry.getMicroControllerByID(pioRequest.getBoardId());
        if (dispatchers.isPresent()) {
            final BaseBoardCommand baseBoardCommand = new BaseBoardCommand(Arrays.asList(dispatchers.get()), pioRequest);
            messageExecutor.execute(baseBoardCommand);

        } else {


            // TODO: if board isn't  subscribed, notify initiating dispatcher
//            pioRequest.getInitiatingSubscriber()
            logger.warn("Cannot find subscribed board with id " + pioRequest.getBoardId() );
        }
    }

    void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    void setMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
