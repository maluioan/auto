package com.home.automation.homeboard.websocket.message.request;

import com.home.automation.homeboard.websocket.Subscriber;

import java.util.Optional;


public interface WSRequest
{
	Optional<String> getExecutionId();

	void setReceivingSubscriber(Subscriber subscriber);

	Subscriber getReceivingSubscriber();

	Object getPayload();
}
