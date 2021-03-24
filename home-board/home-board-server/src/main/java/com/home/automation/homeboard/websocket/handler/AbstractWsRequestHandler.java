package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.mediator.MessageMediator;
import com.home.automation.homeboard.websocket.message.request.BoardRequestMessage;
import com.home.automation.homeboard.websocket.message.request.WSRequest;

import java.util.function.Function;


public abstract class AbstractWsRequestHandler<MSG extends WSRequest, ENT extends BoardRequestMessage>
		implements WsRequestHandler<MSG>
{
	private MessageMediator messageMediator;
	private Function<MSG, ENT> converter;

	@Override
	public void handle(MSG wsMEssage)
	{
		ENT apply = converter.apply(wsMEssage);
		if (apply == null) {
			throw new IllegalStateException("Cannot convert: " + wsMEssage.getExecutionId());
		}
		this.handleInternal(apply);
	}

	protected abstract void handleInternal(ENT entity);

	public void addConverter(final Function<MSG, ENT> converter) {
		this.converter = converter;
	}

	protected MessageMediator getMessageMediator()
	{
		return messageMediator;
	}

	public void setMessageMediator(MessageMediator messageMediator)
	{
		this.messageMediator = messageMediator;
	}
}
