package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.command.MessageExecutor;

public final class BoardMessageMediatorFactory {
    private static final Object LOCK = new Object();
    private static BoardMessageMediator BOARD_MSG_MEDIATOR;

    public BoardMessageMediator createMediator() {
        if (BOARD_MSG_MEDIATOR != null) {
            return BOARD_MSG_MEDIATOR;
        } else {
            synchronized (LOCK) {
                if (BOARD_MSG_MEDIATOR == null) {
                    BOARD_MSG_MEDIATOR = new BoardMessageMediator();
                }
            }
        }
        return BOARD_MSG_MEDIATOR;
    }

    public void setSubscriberRegistry(final SubscriberRegistry subscriberRegistry) {
        final BoardMessageMediator mediator = createMediator();
        mediator.setSubscriberRegistry(subscriberRegistry);
    }

    public void setMessageExecutor(final MessageExecutor messageExecutor) {
        final BoardMessageMediator mediator = createMediator();
        mediator.setMessageExecutor(messageExecutor);
    }
}
