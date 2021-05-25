package com.home.automation.dispatcher.config;

import com.home.automation.dispatcher.messages.handler.CustomStompFrameMessageHandler;
import com.home.automation.dispatcher.messages.handler.frame.FeedbackSubscriptionFrameHandler;
import com.home.automation.dispatcher.messages.handler.frame.FrameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;

@Configuration
public class CustomDelegatingWebSocketMessageBrokerConfiguration extends DelegatingWebSocketMessageBrokerConfiguration {

    @Bean
    public CustomStompFrameMessageHandler customMessageHandler(AbstractSubscribableChannel clientInboundChannel, AbstractSubscribableChannel clientOutboundChannel, CompositeMessageConverter brokerMessageConverter) {
        final CustomStompFrameMessageHandler handler = new CustomStompFrameMessageHandler(clientInboundChannel);
        handler.addSubscriptionHandler(feedbackSubscriptionFrameHandler(clientOutboundChannel, brokerMessageConverter));
        return handler;
    }

    @Bean
    public FrameHandler feedbackSubscriptionFrameHandler(final AbstractSubscribableChannel clientOutboundChannel, CompositeMessageConverter brokerMessageConverter) {
        final FeedbackSubscriptionFrameHandler feedbackSubscriptionFrameHandler = new FeedbackSubscriptionFrameHandler(clientOutboundChannel);
        feedbackSubscriptionFrameHandler.setFeedbackSubscription("/feedback");
        feedbackSubscriptionFrameHandler.setOutboundMessageConverters(brokerMessageConverter);
        return feedbackSubscriptionFrameHandler;
    }

    // TODO: daca vrem imediat dupa subscriptie sa trimitem mesajul, acum, cum e in 'customMessageHandler', o sa trimitem msg-ul in paralel cu subscriptia
//    public AbstractBrokerMessageHandler simpleBrokerMessageHandler(AbstractSubscribableChannel clientInboundChannel, AbstractSubscribableChannel clientOutboundChannel, AbstractSubscribableChannel brokerChannel, UserDestinationResolver userDestinationResolver)
}
