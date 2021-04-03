package com.home.automation.homeboard.websocket.message.request;

import com.home.automation.homeboard.websocket.Subscriber;

import java.util.Optional;


public interface WSRequest<PAYLOAD>
{
	Optional<String> getMessageId();

	void setInitiatingSubscriber(Subscriber subscriber);

	Subscriber getInitiatingSubscriber();

	PAYLOAD getPayload();
}
