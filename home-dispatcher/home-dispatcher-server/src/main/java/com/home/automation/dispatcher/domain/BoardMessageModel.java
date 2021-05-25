package com.home.automation.dispatcher.domain;

import com.home.automation.dispatcher.wsclient.messages.MessageType;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = BoardMessageModel.FIND_MESSAGE_BY_MESSAGE_ID,
                query = "select object (bmsg) from boardmessages as bmsg where bmsg.messageId = :messageId"
        ),
        @NamedQuery(
                name = BoardMessageModel.FIND_LAST_MESSAGES_BY_COUNT,
                query = "select object (bmsg) from boardmessages as bmsg order by bmsg.id desc" // TODO: add created date desc, cand o sa mearga created date
        )
})
@Entity(name = "boardmessages")
public class BoardMessageModel extends BaseModel {

    public static final String FIND_MESSAGE_BY_MESSAGE_ID = "BoardMessageModel.findByMessageId";
    public static final String FIND_LAST_MESSAGES_BY_COUNT = "BoardMessageModel.findLastMessagesByCount";

    @NaturalId
    @Column(name = "messageId", unique = true, nullable = false)
    private String messageId;

    @Column(name = "executorId")
    private String executorId;

    @Column(name = "actionId")
    private String actionId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "payload")
    private String payload;

    @Column(name = "message_type")
    @Enumerated(EnumType.STRING) // TODO: change from string to number
    private MessageType messageType;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
