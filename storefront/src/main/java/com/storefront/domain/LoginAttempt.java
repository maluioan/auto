package com.storefront.domain;

import com.storefront.login.data.FailureReason;

import javax.persistence.*;

@Entity(name = "logins")
public class LoginAttempt extends Base {

    @Column(unique = true, name = "uid")
    private String uid;

    @Column(name = "success")
    private Boolean success;

    @Enumerated(EnumType.STRING)
    private FailureReason failureReason;

    private String failureMessage;

    @Column(name = "attempted_username")
    private String usedClientId;

    @Column(name = "attempted_password")
    private String usedClientSecret;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public String getUsedClientId() {
        return usedClientId;
    }

    public void setUsedClientId(String usedClientId) {
        this.usedClientId = usedClientId;
    }

    public String getUsedClientSecret() {
        return usedClientSecret;
    }

    public void setUsedClientSecret(String usedClientSecret) {
        this.usedClientSecret = usedClientSecret;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
