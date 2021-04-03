package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


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
		final String supportedVersion = findAcceptedVersion(wsMessage);

		final Map<String, Object> headers = new HashMap<>();
		headers.put("version", supportedVersion); // TODO: add heart-beat header and treat it in a handler -- check DefaultStompSession linia 342

		final StompRequest connectedMessage = new StompRequest();
		connectedMessage.setMessageType(MessageType.CONNECTED);
		connectedMessage.setMessageHeaders(headers);
		wsMessage.getInitiatingSubscriber().sendMessage(connectedMessage);
	}

	private String findAcceptedVersion(StompRequest wsMessage) {
		// TODO: check version specifications and differences 1.0 -> 1.1 -> 1.2
		String supportedVersion = null;
		final Object versions = wsMessage.getMessageHeaders().get("accept-version");
		if (versions != null) {
			final String[] versionsSplit = StringUtils.split(versions.toString(), ",");
			supportedVersion = (versionsSplit.length > 0) ? versionsSplit[versionsSplit.length - 1] : null;
		}
		return supportedVersion != null ? supportedVersion : "1.0";
	}
}
