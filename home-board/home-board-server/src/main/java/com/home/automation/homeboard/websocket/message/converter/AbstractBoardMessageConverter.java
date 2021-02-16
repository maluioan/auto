package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.BaseBoardMessage;

import java.util.Optional;

public abstract class AbstractBoardMessageConverter<T extends BaseBoardMessage> {

    private AbstractBoardMessageConverter nextMessageConverter;

    public void setNextMessageConverter(AbstractBoardMessageConverter nextMessageConverter) {
        this.nextMessageConverter = nextMessageConverter;
    }

    public abstract Optional<T> handleMessage(String msg);

    protected Optional<T> next(String msg) {
        return hasNext() ? (Optional<T>)nextMessageConverter.handleMessage(msg) : null;
    }

    protected boolean hasNext() {
        return nextMessageConverter != null;
    }
}
