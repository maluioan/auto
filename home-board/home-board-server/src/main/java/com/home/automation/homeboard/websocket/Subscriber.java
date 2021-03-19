package com.home.automation.homeboard.websocket;

import java.io.IOException;

/**
 *
 */
public interface Subscriber {

    enum Type {
        MICROCONTROLLER,
        DISPATCHER
    }

    /**
     *
     * @return
     */
    String getIdentifier();

    /**
     *
     * @return
     */
//    String getSubscriptionId();

    /**
     *
     * @param payload
     */
    default void sendMessage(Object payload) {
        sendMessage(payload, false);
    }

    /**
     *
     * @param payload
     * @param asynch
     */
    void sendMessage(Object payload, boolean asynch);

    /**
     *
     * @throws IOException
     */
    void disconnect() throws IOException;
    /**
     *
     * @return
     */
    Type getType();

}
