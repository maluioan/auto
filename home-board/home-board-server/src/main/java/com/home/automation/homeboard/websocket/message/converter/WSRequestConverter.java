package com.home.automation.homeboard.websocket.message.converter;

import com.home.automation.homeboard.websocket.message.request.WSRequest;

import java.util.Optional;

public interface WSRequestConverter<T extends WSRequest> {

    Optional<T> decodeMessage(String msg);

    String encodeMessage(T request);

    interface TypeConverterHelper<String, T> {
        String encodeMessage(T payload);

        T decodeMessage(String payload);
    }
}
