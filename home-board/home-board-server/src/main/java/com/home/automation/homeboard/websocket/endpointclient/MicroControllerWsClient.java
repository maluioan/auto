package com.home.automation.homeboard.websocket.endpointclient;

import com.home.automation.homeboard.websocket.message.MicroControllerMessage;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public class MicroControllerWsClient extends AbstractWsClient<MicroControllerMessage> {

    private String boardId;

    @Override
    protected void onOpenInternal(Session session) {
        this.boardId = super.getHeaderValue("bid");
        if (this.boardId == null) {
            treatNoIdBoardConnection();
        }
        getSubscriberRegistry().registerMicroControllerSubscriber(this);
        logger.info(String.format("Board with id %s, connected", boardId));
    }

    private void treatNoIdBoardConnection() {
        logger.error("Board didn't specified an id, please specify one!");
        super.disconnect();
        throw new IllegalArgumentException("Board connection withoout an id");
    }

    @Override
    protected void onMessageInternal(final MicroControllerMessage microControllerMessage) {
        logger.info("am primit msg-ul de la board: " + microControllerMessage.getBoardId() + ", cu " + microControllerMessage.getType());
        getMessageMediator().handleDispatchMessage(microControllerMessage);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        super.onClose(session, closeReason);
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