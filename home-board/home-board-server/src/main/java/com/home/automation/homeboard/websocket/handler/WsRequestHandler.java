package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.request.WSRequest;


public interface WsRequestHandler<MSG extends WSRequest>
{
	boolean canHandle(MSG wsMessage);

	void handle(MSG wsMEssage);
}
