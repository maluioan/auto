package com.home.automation.homeboard.websocket.endpoint;


import com.home.automation.homeboard.websocket.message.MicroControllerMessage;
import org.apache.commons.collections4.CollectionUtils;

import javax.websocket.*;
import java.util.List;

//https://abhishek-gupta.gitbook.io/java-websocket-api-handbook/configuration#configurators
//@ServerEndpoint(
//        value = "/boards/server",
////        encoders = CustomEncoder.class,
//        decoders = CustomDecoder.class,
//        subprotocols = "COI",
//        configurator = CustomServerEndpointConfig.class
//)
public class WebSocketBoardSubscribedClient extends BoardWsSubscribedClient<MicroControllerMessage> {

    private String boardId;

    @Override
    protected void onOpenInternal(Session session) {
        this.boardId = findBoardId();
        System.out.println( String.format("board with id %s connected, with sesId %s", boardId, session.getId()));
    }

    @Override
    protected void onMessageInternal(final MicroControllerMessage baseBoardMessage) {
        System.out.println("am primit msg-ul de la board: " + baseBoardMessage.getBoardId() + ", cu " + baseBoardMessage.getType());

//        wsSession.getAsyncRemote().sendText(baseBoardMessage.getPayload().toString());
    }

    public void onClose(Session session, CloseReason closeReason) {
        this.wsSession = null;
    }

    private String findBoardId() {
        final List<String> bids = super.handshakeRequest.getHeaders().get("bid");
        return (CollectionUtils.isNotEmpty(bids)) ? bids.get(0) : null;
    }

}
