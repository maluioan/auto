package com.home.automation.dispatcher.service;

import com.home.automation.dispatcher.domain.BoardMessageModel;
import com.home.automation.dispatcher.messages.ActionMessage;
import com.home.automation.dispatcher.messages.FeedbackMessageData;
import com.home.automation.dispatcher.service.domain.BoardMessageService;
import com.home.automation.dispatcher.wsclient.messages.MessageDispatcherService;
import com.home.automation.homeboard.ws.ActionMessagePayload;
import com.home.automation.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: create an interface, refactor etc
@Service
public class BoardMessageHandlerService {

    @Autowired
    private MessageDispatcherService messageDispatcherService;

    @Autowired
    private BoardMessageService boardMessageService;

    public void storeAndExecuteMessage(ActionMessage message, String name) {
        final BoardMessageModel boardMessage = storeMessage(message, name);

        messageDispatcherService.sendFeedbackMessageToSubscribers(createFrameFeedback(boardMessage), message.getActionId());
        if (StringUtils.isNoneBlank(boardMessage.getExecutorId())) {
            messageDispatcherService.sendMessageToBoard(convertToBoardMessage(boardMessage), boardMessage.getMessageId());
        }
    }

    private BoardMessageModel storeMessage(final ActionMessage message, final String userName) {
        final BoardMessageModel bmm = new BoardMessageModel();
        bmm.setExecutorId(message.getExecutorId());
        bmm.setMessageId(CommonUtils.createRandomSixDigitsId());
        bmm.setPayload(String.valueOf(message.getPayload()));
        bmm.setUserName(userName);
        bmm.setActionId(message.getActionId());

        return boardMessageService.saveBoardMessageModel(bmm);
    }

    private FeedbackMessageData createFrameFeedback(final BoardMessageModel boardMessage) {
        FeedbackMessageData fmd = new FeedbackMessageData();
        fmd.setUserName(boardMessage.getUserName());
        fmd.setActionName(boardMessage.getActionId());
        fmd.setMessage(boardMessage.getPayload());
        fmd.setCreationDate(CommonUtils.convertTimeToString(boardMessage.getDateCreated(), CommonUtils.DAY_MONTH_TIME_PATTERN));
        return fmd;
    }

    private ActionMessagePayload convertToBoardMessage(final BoardMessageModel boardMessage) {
        final ActionMessagePayload actionPayload = new ActionMessagePayload();
        actionPayload.setExecutorId(boardMessage.getExecutorId());
        actionPayload.setMessageId(boardMessage.getMessageId());
        actionPayload.setPayload(boardMessage.getPayload());
        return actionPayload;
    }

}
