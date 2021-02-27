package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;

import java.util.List;
import java.util.Optional;

public interface SubscriberRegistry {

    boolean registerMicroControllerSubscriber(Subscriber microController);

    boolean removeMicroControllerSubscriber(Subscriber microController);

    boolean registerDispatcherSubscriber(Subscriber dispatcher);

    boolean removeDispatcherSubscriber(Subscriber dispatcher);

    List<Subscriber> getDispatchers();

    Optional<Subscriber> getMicrControllerByID(String id);
}
