package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.StompRequest;


public class ConnectWsRequestHandler implements WsRequestHandler<StompRequest>
{
	@Override
	public boolean canHandle(StompRequest wsMessage)
	{
		return MessageType.CONNECT.equals(wsMessage.getMessageType());
	}

	@Override
	public void handle(final StompRequest wsMessage)
	{
		final StompRequest connectedMessage = new StompRequest();
		connectedMessage.setMessageType(MessageType.CONNECTED);
		wsMessage.getReceivingSubscriber().sendMessage(connectedMessage);
	}
}
