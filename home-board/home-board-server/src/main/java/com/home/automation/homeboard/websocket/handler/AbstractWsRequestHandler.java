package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.exception.BoardServiceException;
import com.home.automation.homeboard.websocket.mediator.MessageMediator;
import com.home.automation.homeboard.websocket.message.request.WSRequest;
import com.home.automation.homeboard.ws.WSMessagePayload;


public abstract class AbstractWsRequestHandler<MSG extends WSRequest>
		implements WsRequestHandler<MSG>
{
	private MessageMediator messageMediator;

	@Override
	public void handle(MSG wsMEssage)
	{
		try {
			this.handleInternal(wsMEssage);
		} catch (Exception e) {
			final BoardServiceException boardServiceException = new BoardServiceException(e.getMessage(), e);
			boardServiceException.setPayload((WSMessagePayload)wsMEssage.getPayload());
			throw boardServiceException;
		}
	}

	protected abstract void handleInternal(MSG request);

	protected MessageMediator getMessageMediator()
	{
		return messageMediator;
	}

	public void setMessageMediator(MessageMediator messageMediator)
	{
		this.messageMediator = messageMediator;
	}
}
