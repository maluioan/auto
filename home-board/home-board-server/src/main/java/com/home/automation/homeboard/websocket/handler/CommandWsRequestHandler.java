package com.home.automation.homeboard.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.data.BoardData;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.service.HomeBoardService;
import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.CommandRequestMessage;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;


public class CommandWsRequestHandler extends AbstractWsRequestHandler<StompRequest, CommandRequestMessage>
{

	@Autowired
	private HomeBoardService homeBoardService;

	private ObjectMapper mapper;
	private String commandDestination;

	public CommandWsRequestHandler() {
		super.addConverter((stompRequest) -> {
			Object originalPayload = stompRequest.getPayload();
			try
			{
				return mapper.readValue(originalPayload.toString(), CommandRequestMessage.class);
			}
			catch (JsonProcessingException e)
			{
				// TODO: treat exceptions
				e.printStackTrace();
			}
			return null;
		});
	}

	@Override
	public boolean canHandle(final StompRequest wsMessage)
	{
		if (!MessageType.MESSAGE.equals(wsMessage.getMessageType())) {
			return false;
		}

		if (!(wsMessage.getPayload() instanceof String)) {
			return false;
		}

		final Optional<String> destination = wsMessage.getDestination();
		if (!destination.isPresent()) {
			return false;
		}
		return StringUtils.equals(commandDestination, destination.get()); // TODO: check destination
	}

	@Override
	protected void handleInternal(final CommandRequestMessage commandRequest)
	{
		final String commandId = commandRequest.getCommandId();
		if (StringUtils.isBlank(commandId)) {
			throw new IllegalStateException("Command id must be supplied.");
		}
		final CommandData command = homeBoardService.findCommandById(Long.valueOf(commandId));

		final MultiValueMap<BoardData, ActionData> boardToActionMap = new LinkedMultiValueMap<>();
		CollectionUtils.emptyIfNull(command.getActions())
				.forEach(actionData -> boardToActionMap.add(actionData.getBoard(), actionData));

		boardToActionMap.entrySet().forEach(entrySet -> sendMessage(entrySet.getKey(), entrySet.getValue()));

		getMessageMediator().handleDispatchMessage(null);
	}

	private void sendMessage(BoardData key, List<ActionData> actions)
	{
		// TODO: create request with all actions based on board
		getMessageMediator().handleDispatchMessage(null);
	}

	public void setMapper(ObjectMapper mapper)
	{
		this.mapper = mapper;
	}

	public void setCommandDestination(String commandDestination)
	{
		this.commandDestination = commandDestination;
	}
}
