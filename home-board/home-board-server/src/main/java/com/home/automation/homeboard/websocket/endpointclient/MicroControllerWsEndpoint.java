package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.websocket.handler.WsRequestHandler;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import org.apache.commons.collections4.CollectionUtils;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MicroControllerWsEndpoint extends AbstractWsEndpoint<PioRequest>
{
    private String boardId;

    @Override
    protected void onOpenInternal(Session session) {
        final Optional<String> boardOpt = super.getHeaderValue("bid");
        boardOpt.orElseThrow(() -> treatNoIdBoardConnection());
        this.boardId = boardOpt.get();
        getSubscriberRegistry().registerMicroControllerSubscriber(this);
        logger.info(String.format("Board with id %s, connected", boardId));
    }

    private RuntimeException treatNoIdBoardConnection() {
        logger.error("Board didn't specified an id, please specify one!");
        super.disconnect();
        return new IllegalArgumentException("Board connection without an id");
    }

    @Override
    protected void onMessageInternal(final PioRequest pioRequest) {
        // TODO: in frame, seteaza messageId-ul (in cadrul headerului)
//        logger.info("am primit msg-ul de la board: " + microControllerMessage.getBoardId() + ", cu " + microControllerMessage.getType());
//        getMessageMediator().handleDispatchMessage(microControllerMessage);
        final List<WsRequestHandler<PioRequest>> requestHandllers = getRequestMessageHandlers().stream()
                .filter(handler -> handler.canHandle(pioRequest)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(requestHandllers)) {
            requestHandllers.forEach(handler -> handler.handle(pioRequest));

        } else {
            logger.warn("No handler was found for request: " + pioRequest.getMessageId());
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        // TODO: dac board-ul se deconecrteaza, nu 'anunta' board-ul..
        super.onClose(session, closeReason);
        logger.warn(String.format("Board with ID %s left", session.getId()));
        getSubscriberRegistry().removeMicroControllerSubscriber(this);
    }

    @Override
    public String getIdentifier() {
        return boardId;
    }

    @Override
    public Type getType() {
        return Type.MICROCONTROLLER;
    }
}

//https://abhishek-gupta.gitbook.io/java-websocket-api-handbook/configuration#configurators
//@ServerEndpoint(
//        value = "/boards/server",
////        encoders = CustomEncoder.class,
//        decoders = CustomDecoder.class,
//        subprotocols = "COI",
//        configurator = CustomServerEndpointConfig.class
//)
