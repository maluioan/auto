package com.home.automation.homeboard.websocket.message.request;


import java.util.Optional;


// TODO: implement
public class PioRequest extends AbstractWSRequest<Object>
{
	private String boardId;
	private String messageId;
	private String executorId;

	@Override
	public Optional<String> getMessageId()
	{
		return Optional.ofNullable(messageId);
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getBoardId()
	{
		return boardId;
	}

	public String getExecutorId() {
		return executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}
}
