package com.home.automation.dispatcher.wsclient.frame.handlers;

import com.home.automation.dispatcher.domain.BoardMessageModel;
import com.home.automation.dispatcher.messages.FeedbackMessageData;
import com.home.automation.dispatcher.service.domain.BoardMessageService;
import com.home.automation.dispatcher.wsclient.messages.BoardStompMessage;
import com.home.automation.dispatcher.wsclient.messages.MessageDispatcherService;
import com.home.automation.homeboard.ws.NonSubscribedBoardMessagePayload;
import com.home.automation.homeboard.ws.WSMessagePayload;
import com.home.automation.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class NonSubscribedBoardBoardFrameHandler implements BoardFrameHandler {

    @Autowired
    private BoardMessageService boardMessageService;

    @Autowired
    private MessageDispatcherService messageDispatcherService;

    @Override
    public boolean canHandleFrame(BoardStompMessage stompMessage) {
        final WSMessagePayload messagePayload = stompMessage.getPayload();
        return (messagePayload != null) && messagePayload.getClass().isAssignableFrom(NonSubscribedBoardMessagePayload.class);
    }

    @Override
    public void handleFrame(final BoardStompMessage stompMessage) {
        final BoardMessageModel bmm = getBoardMessageModel(stompMessage);
        if (bmm != null) {
            final NonSubscribedBoardMessagePayload payload = (NonSubscribedBoardMessagePayload)stompMessage.getPayload();
            final Object msgPayload = payload.getPayload();

            final List<FeedbackMessageData> feedbacks = payload.getActionIds().stream()
                    .map(actionID -> createFeedbackMessageData(bmm, actionID, msgPayload))
                    .collect(Collectors.toList());
            messageDispatcherService.sendFeedbackMessageToUser(feedbacks, bmm.getUserName());
        }
    }

    private FeedbackMessageData createFeedbackMessageData(final BoardMessageModel bmm, final String actionID, final Object msgPayload) {
        final FeedbackMessageData fmd = new FeedbackMessageData();
        fmd.setUserName(bmm.getUserName());
        fmd.setActionName(actionID); // TODO: obtine ceva info mai relevanta pt actiune
        fmd.setMessage(String.valueOf(msgPayload));
        fmd.setType("unsubscribed");
        fmd.setCreationDate(CommonUtils.convertTimeToString(bmm.getDateCreated(), CommonUtils.DAY_MONTH_TIME_PATTERN));
        return fmd;
    }

    private BoardMessageModel getBoardMessageModel(BoardStompMessage stompMessage) {
        return this.boardMessageService.getBoardMessageByMessageId(stompMessage.getHeaders().getMessageId());
    }
}
