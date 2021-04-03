package com.home.automation.homeboard.websocket.mediator;

import com.home.automation.homeboard.websocket.Subscriber;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BoardSubscriberRegistry implements SubscriberRegistry {

    protected static final Logger logger = LogManager.getLogger(SubscriberRegistry.class);

    private Map<String, Subscriber> dispatchers = MapUtils.synchronizedMap(new HashMap<>());

    private Map<String, Subscriber> mcSubscribers = new ConcurrentHashMap<>();

    @Override
    public boolean registerMicroControllerSubscriber(final Subscriber microController) {
        Assert.notNull(microController, "MicroController subscriber cannot be null");
        boolean registered = false;

        final String mcId = microController.getIdentifier();
        if (!mcSubscribers.containsKey(mcId)) {
            registered = mcSubscribers.put(mcId, microController) != null;

        } else {
            logger.warn(String.format("MicroController %s already exists", mcId));
        }
        return registered;
    }

    @Override
    public boolean removeMicroControllerSubscriber(Subscriber microController) {
        Assert.notNull(microController, "MicroController subscriber cannot be null to remove");
        return mcSubscribers.remove(microController.getIdentifier()) != null;
    }

    @Override
    public boolean registerDispatcherSubscriber(final Subscriber dispatcher) {
        Assert.notNull(dispatcher, "Dispatcher subscriber cannot be null");

        boolean registered = false;
        if (!dispatchers.containsKey(dispatcher.getIdentifier())) {
            registered = dispatchers.put(dispatcher.getIdentifier(), dispatcher) != null;

        } else {
            logger.warn(String.format("Dispatcher subscriber %s is already present: ", dispatcher.getIdentifier()));
        }
        return registered;
    }

    @Override
    public Subscriber removeDispatcherSubscriber(String dispatcherId) {
        Assert.notNull(dispatcherId, "Dispatcher subscriber cannot be null");
        return dispatchers.remove(dispatcherId);
    }

    @Override
    public List<Subscriber> getDispatchersSubscribers() {
        return new ArrayList<>(dispatchers.values());
    }

    @Override
    public Optional<Subscriber> getMicroControllerByID(String id) {
        return Optional.ofNullable(this.mcSubscribers.get(id));
    }

    @Override
    public boolean isMicroControllerBoardSubscribed(String microBoardId) {
        return this.mcSubscribers.containsKey(microBoardId);
    }
}
