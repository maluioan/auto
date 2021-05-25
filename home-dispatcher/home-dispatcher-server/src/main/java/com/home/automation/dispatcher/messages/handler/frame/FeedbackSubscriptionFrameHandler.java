package com.home.automation.dispatcher.messages.handler.frame;

import com.home.automation.dispatcher.domain.BaseModel;
import com.home.automation.dispatcher.domain.BoardMessageModel;
import com.home.automation.dispatcher.messages.FeedbackMessageData;
import com.home.automation.dispatcher.service.domain.BoardMessageService;
import com.home.automation.dispatcher.wsclient.messages.MessageDispatcherService;
import com.home.automation.util.CommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackSubscriptionFrameHandler implements FrameHandler {

    private final AbstractMessageSendingTemplate<String> messagingTemplate;
    private String feedbackSubscription;
    @Autowired
    private MessageDispatcherService messageDispatcherService;

    @Autowired
    private BoardMessageService boardMessageService;

    public FeedbackSubscriptionFrameHandler(final AbstractSubscribableChannel clientOutboundChannel) {
        Assert.notNull(clientOutboundChannel, "Client putbound channel should not be empty!");
        this.messagingTemplate = new SimpMessagingTemplate(clientOutboundChannel);
    }

    @Override
    public boolean canHandleFrame(Message<?> message) {
        final String destination = SimpMessageHeaderAccessor.getDestination(message.getHeaders());
        return StringUtils.endsWith(destination, feedbackSubscription);
    }

    @Override
    public void handleFrame(Message<?> message) {
        final Principal principal = SimpMessageHeaderAccessor.getUser(message.getHeaders());

        if (principal != null && StringUtils.isNotBlank(principal.getName())) {
            final List<FeedbackMessageData> feedBackMessages = getFeedBackMessages();

            if (CollectionUtils.isNotEmpty(feedBackMessages)) {
                messageDispatcherService.sendFeedbackMessageToUser(feedBackMessages, principal.getName());
            }
        }
    }

    private List<FeedbackMessageData> getFeedBackMessages() {
        final Collection<BoardMessageModel> lastBoardMessages = this.boardMessageService.findLastBoardMessages(10);
        return CollectionUtils.emptyIfNull(lastBoardMessages).stream()
                .sorted(Comparator.comparing(BaseModel::getDateCreated))
                .map(this::createFeedbackMessageData)
                .collect(Collectors.toList());
    }

    private FeedbackMessageData createFeedbackMessageData(final BoardMessageModel boardMessage) {
        final FeedbackMessageData fmd = new FeedbackMessageData();
        fmd.setUserName(boardMessage.getUserName());
        fmd.setActionName(boardMessage.getExecutorId()); // TODO: gaseste ceva detaliu mai relevant pt actiune
        fmd.setMessage(boardMessage.getPayload());
        fmd.setCreationDate(CommonUtils.convertTimeToString(boardMessage.getDateCreated(), CommonUtils.DAY_MONTH_TIME_PATTERN));
        return fmd;
    }

    public void setFeedbackSubscription(String feedbackSubscription) {
        this.feedbackSubscription = feedbackSubscription;
    }

    public void setOutboundMessageConverters(CompositeMessageConverter messageCovnerter) {
        Assert.notNull(messageCovnerter, "Message converter cannot be null");
        this.messagingTemplate.setMessageConverter(messageCovnerter);
    }
}
