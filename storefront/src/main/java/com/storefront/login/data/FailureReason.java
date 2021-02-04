package com.storefront.login.data;

public enum FailureReason {

    /**
     * UserData is disabled
     */
    DISABLED,

    /**
     * Unexpected error
     */
    INTERNAL_ERROR,

    /**
     * Invalid credentials
     */
    BAD_CREDENTIALS,

    /**
     * UserData not found
     */
    NOT_FOUND,

    /**
     * UserData not found
     */
    UNAUTHORIZED,

    /**
     * Expired credentials
     */
    CREDENTIALS_EXPIRED,

    /**
     * Unknown failure
     */
    UNKNOWN

}
