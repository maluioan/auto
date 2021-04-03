package com.home.automation.homeboard.websocket.message.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.automation.homeboard.exception.BoardServiceException;
import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.CommandMessagePayload;
import com.home.automation.homeboard.ws.SimpleWSMessagePayload;
import com.home.automation.homeboard.ws.WSMessagePayload;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StompRequestConverter implements WSRequestConverter<StompRequest>
{
	private static final String NONE = "NONE/NONE";
	private Map<MediaType, TypeConverterHelper<String, WSMessagePayload>> payloadConverters = new HashMap<>();

	private ObjectMapper mapper;

	@PostConstruct
	public void afterPropertiesSet() {
		payloadConverters.put(MediaType.APPLICATION_JSON, new JsonConverter());
		payloadConverters.put(MediaType.valueOf(NONE), new SimpleMessageConverter());
	}

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
	public String encodeMessage(final StompRequest request)	{
		Assert.notNull(request.getMessageType(), "Type should not be null for decoding");

		final String headers = MapUtils.emptyIfNull(request.getMessageHeaders()).entrySet().stream()
				.map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue()))
				.collect(Collectors.joining("\n"));

		final StringBuilder builder = new StringBuilder(request.getMessageType().name()).append("\n");
		if (StringUtils.isNotEmpty(headers)) {
			builder.append(headers).append("\n\n");
		}
		if (request.getPayload() != null) {
			builder.append(findPayloadConverter(request).encodeMessage(request.getPayload()));
		}

		builder.append("\0");
		return builder.toString();
	}

	public void setMapper(ObjectMapper mapper) {
		Assert.notNull(mapper, "mapper should not be null");
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.mapper = mapper;
	}

	private StompRequest createInitialStompMessage(final MessageType messageType, final Object msg) {
		final String[] stompParts = msg.toString().split("\n\n");
		if (stompParts.length < 2) {
			return null;
		}

		final StompRequest stompRequest = new StompRequest();
		stompRequest.setMessageType(messageType);
		stompRequest.setMessageHeaders(createMapHeader(stompParts[0]));
		stompRequest.setPayload(findPayloadConverter(stompRequest).decodeMessage(stompParts[1]));
		return stompRequest;
	}

	private TypeConverterHelper<String, WSMessagePayload> findPayloadConverter(final StompRequest stompRequest) {
		final Optional<String> contentType = stompRequest.getContentType();
		// TODO: daca content type-ul nu are un convertor
		final MediaType mediaType = contentType.map(MediaType::valueOf).orElse(MediaType.valueOf(NONE));
		return payloadConverters.containsKey(mediaType)
				? payloadConverters.get(mediaType)
				: payloadConverters.get(MediaType.valueOf(NONE));
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

	private class JsonConverter implements TypeConverterHelper<String, WSMessagePayload> {
		@Override
		public String encodeMessage(final WSMessagePayload payload) {
			try
			{
				return mapper.writeValueAsString(payload);
			}
			catch (JsonProcessingException e)
			{
				throw new BoardServiceException("Cannot parse json payload", e);
			}
		}

		@Override
		public CommandMessagePayload decodeMessage(String payload) {
			try
			{
				return mapper.readValue(String.valueOf(payload), CommandMessagePayload.class);
			}
			catch (JsonProcessingException e)
			{
				throw new BoardServiceException("Cannot parse json payload", e);
			}
		}
	}

	private class SimpleMessageConverter implements TypeConverterHelper<String, WSMessagePayload> {
		@Override
		public String encodeMessage(WSMessagePayload payload) {
			return SimpleWSMessagePayload.class.equals(payload.getType())
					? ((SimpleWSMessagePayload)payload).getPayload()
					: null;
		}

		@Override
		public WSMessagePayload decodeMessage(String payload) {
			return new SimpleWSMessagePayload(payload);
		}
	}
}
