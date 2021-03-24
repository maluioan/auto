package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.StompRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StompRequestConverter implements WSRequestConverter<StompRequest>
{
	@Override
	public Optional<StompRequest> decodeMessage(final String message) {
		final String[] split = message.split("\n");
		if (split.length < 1) {
			return Optional.empty();
		}

		final Optional<MessageType> messageType = findMessageType(split[0]);
		return messageType.map(type -> createInitialStompMessage(type, message));
	}

	@Override
	public String encodeMessage(final StompRequest request)
	{
		final String headers = request.getMessageHeaders().entrySet().stream()
				.map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
				.collect(Collectors.joining("\n"));

		return new StringBuilder(request.getMessageType().name())
				.append("\n").append(headers).append("\n\n")
				.append(request.getPayload()).toString();
	}

	private StompRequest createInitialStompMessage(final MessageType messageType, final Object msg) {
		final String[] stompParts = msg.toString().split("\n\n");
		if (stompParts.length < 2) {
			return null;
		}

		final StompRequest stompRequest = new StompRequest();
		stompRequest.setMessageType(messageType);
		stompRequest.setMessageHeaders(createMapHeader(stompParts[0]));
		stompRequest.setPayload(stompParts[1]);
		return stompRequest;
	}

	private Optional<MessageType> findMessageType(final String command) {
		try {
			return Optional.of(MessageType.valueOf(command));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private Map<String, Object> createMapHeader(String commandHeaders) {
		return Arrays.asList(commandHeaders.split("\n")).stream()
				.filter(headerPart -> headerPart.contains(":"))
				.map(headerPart -> headerPart.split(":"))
				.collect(Collectors.toMap(headerPart -> headerPart[0], headerPart -> headerPart[1]));
	}
}





//public class StompMessageConverter implements PiRequestConverter<StompRequest>
//{
//	private ObjectMapper mapper;
//
//	private StompRequestExtractor stompRequestExtractor;
//
//	@Override
//	public Optional<BoardRequestMessage> decodeMessage(final String message) {
//		final Optional<StompRequest> stompRequest = stompRequestExtractor.extractStompRequest(message);
//		return stompRequest.map(this::createBoardRequestMessage);
//	}
//
//	private BoardRequestMessage createBoardRequestMessage(final StompRequest stompRequest) {
//		BoardRequestMessage boardRequestMessage = null;
//
//		if (MessageType.CONNECT.equals(stompRequest.getMessageType())) {
//			boardRequestMessage = new ConnectRequestMessage();
//
//		} else {
//			checkPayloadRequest(stompRequest);
//
//			if ("command-request".equals(stompRequest.getRequestType())) {
//				boardRequestMessage = parsePayload(stompRequest.getOriginalPayload());
//			}
//		}
//
//		if (boardRequestMessage != null) {
//			boardRequestMessage.setStompRequest(stompRequest);
//		}
//		return boardRequestMessage;
//	}
//
//	private void checkPayloadRequest(StompRequest stompRequest)
//	{
//		final Optional<String> requestType = stompRequest.getRequestType();
//		requestType.orElseThrow(() -> new IllegalStateException(
//				String.format("Cannot find request type from message ", stompRequest.getExecutionId())));
//	}
//
//	private BoardRequestMessage parsePayload(final Object originalPayload)
//	{
//		BoardRequestMessage request = null;
//		try
//		{
//			request = mapper.readValue(String.valueOf(originalPayload), CommandRequestMessage.class);
//		}
//		catch (JsonProcessingException e)
//		{
//			e.printStackTrace();
//		}
//		return request;
//	}
//
//	public void setMapper(ObjectMapper mapper)
//	{
//		this.mapper = mapper;
//	}
//
//	public void setStompRequestExtractor(StompRequestExtractor stompRequestExtractor)
//	{
//		this.stompRequestExtractor = stompRequestExtractor;
//	}
//}
