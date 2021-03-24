package com.home.automation.homeboard.websocket.message.request;

import java.util.Optional;


// TODO: implement
public class PioRequest extends AbstractWSRequest
{
	@Override
	public Optional<String> getExecutionId()
	{
		return Optional.empty();
	}

	public String getBoardId()
	{
		return "";
	}
}
