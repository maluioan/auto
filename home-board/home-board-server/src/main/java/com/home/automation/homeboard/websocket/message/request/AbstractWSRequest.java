package com.home.automation.homeboard.websocket.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.automation.homeboard.websocket.Subscriber;
import com.home.automation.homeboard.websocket.message.MessageType;


public abstract class AbstractWSRequest<PAYLOAD> implements WSRequest<PAYLOAD>
{
	@JsonIgnore
	private Subscriber initiatingSubscriber;
	private PAYLOAD payload;
	private MessageType messageType;

	@Override
	public void setInitiatingSubscriber(final Subscriber initiatingSubscriber)
	{
		this.initiatingSubscriber = initiatingSubscriber;
	}

	@Override
	public Subscriber getInitiatingSubscriber()
	{
		return this.initiatingSubscriber;
	}

	@Override
	public PAYLOAD getPayload() {
		return payload;
	}

	public void setPayload(PAYLOAD payload) {
		this.payload = payload;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
}
