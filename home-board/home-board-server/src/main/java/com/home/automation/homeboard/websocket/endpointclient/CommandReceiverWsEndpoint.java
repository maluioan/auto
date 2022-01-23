package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.exception.BoardServiceException;
import com.home.automation.homeboard.websocket.handler.WsRequestHandler;
import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.DefaultMessagePayload;
import org.apache.commons.collections4.CollectionUtils;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CommandReceiverWsEndpoint extends AbstractWsEndpoint<StompRequest>
{
    private String dispatcherId;

    @Override
    protected void onOpenInternal(Session session) {
        final Optional<String> subscriberID = super.getHeaderValue("subscriberID");
        subscriberID.orElseThrow(() -> new IllegalArgumentException("session should have a subscriber Id"));

        dispatcherId = subscriberID.get();
        getSubscriberRegistry().registerDispatcherSubscriber(this);
        logger.info(String.format("Dispatcher with id %s, connected", dispatcherId));
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

    @Override
    protected void onMessageInternal(final StompRequest stompRequest) {
        // TODO: add message validation
        // TODO: add heart-beat handler
        logger.info(String.format("Received message of type %s, from %s ", stompRequest.getMessageType(), dispatcherId));
        try {
            final List<WsRequestHandler<StompRequest>> requestHandllers = getRequestMessageHandlers().stream()
                    .filter(handler -> handler.canHandle(stompRequest)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(requestHandllers)) {
                requestHandllers.forEach(handler -> handler.handle(stompRequest));

            } else {
                logger.warn("No handler was found for request: " + stompRequest.getMessageId());
            }
        } catch (BoardServiceException re) {
            logger.error(String.format("An error occurred processing request with message-id %s",
                    stompRequest.getMessageId()));
            stompRequest.getInitiatingSubscriber().sendMessage(createErrorRequest(stompRequest));
        }
    }

    // TODO: cand se trimite un msg de eroare, se deconecteaza -- DC???
    private StompRequest createErrorRequest(final StompRequest stompRequest) {
        final String errorMsg = String.format("Error performing request with id %s", stompRequest.getMessageId().get());

        // TODO: maybe create a builder for stomp requests
        final StompRequest request = new StompRequest();
        final DefaultMessagePayload simpleWSMessagePayload = new DefaultMessagePayload();
        simpleWSMessagePayload.setPayload(errorMsg);
        request.setPayload(simpleWSMessagePayload);
        request.setContentType("application/json"); // stompRequest.getContentType()
        request.setMessageId(stompRequest.getMessageId().orElse(""));
        request.setMessageType(MessageType.MESSAGE);

        return request;
    }
}
