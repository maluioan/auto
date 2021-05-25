package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.command.BaseBoardCommand;
import com.home.automation.homeboard.websocket.command.MessageExecutor;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import com.home.automation.homeboard.ws.WSMessagePayload;
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
            this.createAndExecuteCommand(dispMessage, dispatchers);
        }
    }

    @Override
    public void handleBoardMessages(final PioRequest pioRequest) {
        final Optional<Subscriber> board = subscriberRegistry.getMicroControllerByID(pioRequest.getBoardId());
        board.ifPresent(subs -> this.createAndExecuteCommand(pioRequest, Arrays.asList(subs)));
    }

    @Override
    public <T extends WSMessagePayload> void sendMessageBackToInitiatingSubscriber(final WSRequest<T> request) {
        final Optional<Subscriber> subscriber = Optional.ofNullable(request.getInitiatingSubscriber());
        subscriber.ifPresent(subs -> createAndExecuteCommand(request, Arrays.asList(subs)));
    }

    private <T extends WSMessagePayload> void createAndExecuteCommand(WSRequest request, List<Subscriber> subs) {
        final BaseBoardCommand baseBoardCommand = new BaseBoardCommand(subs, request);
        messageExecutor.execute(baseBoardCommand);
    }

    void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
        this.subscriberRegistry = subscriberRegistry;
    }

    void setMessageExecutor(MessageExecutor messageExecutor) {
        this.messageExecutor = messageExecutor;
    }
}
