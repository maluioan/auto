package com.storefront.data;

public class LoginData {
    private String failureReason;
    private boolean success;
    private String usedClientId;

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsedClientId() {
        return usedClientId;
    }

    public void setUsedClientId(String usedClientId) {
        this.usedClientId = usedClientId;
    }

}
