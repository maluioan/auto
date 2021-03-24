package com.home.automation.homeboard.websocket.message.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.automation.homeboard.websocket.Subscriber;


public abstract class AbstractWSRequest implements WSRequest
{
	@JsonIgnore
	private Subscriber receivingSubscriber;
	private Object payload;

	@Override
	public void setReceivingSubscriber(final Subscriber receivingSubscriber)
	{
		this.receivingSubscriber = receivingSubscriber;
	}

	@Override
	public Subscriber getReceivingSubscriber()
	{
		return this.receivingSubscriber;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
