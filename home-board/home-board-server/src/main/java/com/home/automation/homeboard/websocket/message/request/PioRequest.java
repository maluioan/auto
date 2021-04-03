package com.home.automation.homeboard.websocket.message.request;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// TODO: implement
public class PioRequest extends AbstractWSRequest<Object>
{
	private String boardId;
	private String messageId;

	private List<String> commands = new ArrayList<>();

	@Override
	public Optional<String> getMessageId()
	{
		return Optional.ofNullable(messageId);
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public List<String> getCommands() {
		return ListUtils.unmodifiableList(commands);
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getBoardId()
	{
		return boardId;
	}

	public void addCommand(final String msg) {
		this.commands.add(msg);
	}

	public void addCommands(final List<String> msgs) {
		this.commands.addAll(msgs);
	}
}
