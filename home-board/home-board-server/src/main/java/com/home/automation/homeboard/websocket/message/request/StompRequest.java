package com.home.automation.homeboard.websocket.message.request;

import com.home.automation.homeboard.ws.WSMessagePayload;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class StompRequest extends AbstractWSRequest<WSMessagePayload> {
    static final String MESSAGE_ID = "message-id";
    static final Consumer<String> EXISTING_MESSAGE_ID =
            (execId) -> {
                throw new IllegalStateException(String.format("Request already contains a message id %s", execId));
            };

    private Map<String, Object> messageHeaders;

    @Override
    public Optional<String> getMessageId()
    {
        return getHeaderValue(MESSAGE_ID);
    }

    public Map<String, Object> getMessageHeaders() {
        if (messageHeaders == null) {
            messageHeaders = new ConcurrentHashMap<>();
        }
        return messageHeaders;
    }

    public void setMessageHeaders(Map<String, Object> messageHeaders) {
        this.getMessageHeaders().putAll(messageHeaders);
    }

    public Optional<String> getRequestType() {
        return getHeaderValue("request-type");
    }

    private <T> Optional<T> getHeaderValue(final String key) {
        return Optional.ofNullable ((T)getMessageHeaders().get(key));
    }

    public Optional<String> getDestination()
    {
        return this.getHeaderValue("destination");
    }

    public Optional<String> getContentType() {
        return this.getHeaderValue("content-type");
    }

    public void addHeader(final String headerKey, final String headerValue) {
        getMessageHeaders().put(headerKey, headerValue);
    }

    public void setMessageId(final String messageIdValue) {
        Assert.isTrue(StringUtils.isNotBlank(messageIdValue), "Please provide a non blank execution id");
        getMessageId().ifPresent(EXISTING_MESSAGE_ID);
        getMessageHeaders().put(MESSAGE_ID, messageIdValue);
    }

    @Override
    public void setPayload(final WSMessagePayload payload) {
        super.setPayload(payload);
        addHeader(WSMessagePayload.PAYLOAD_HEADER, payload.getType().getTypeName());
    }

    public void setContentType(String contentType) {
        this.addHeader("content-type", contentType);
    }
}
