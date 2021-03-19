package com.home.automation.homeboard.websocket.message;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BoardRequestMessage {

    private OriginalBoardMessage orginalMessage;
    private String executionId;
//    private String createdDate; // TODO: ne trebuie ??

    public OriginalBoardMessage getOrginalMessage() {
        return orginalMessage;
    }

    public void setOrginalMessage(OriginalBoardMessage orginalMessage) {
        this.orginalMessage = orginalMessage;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
