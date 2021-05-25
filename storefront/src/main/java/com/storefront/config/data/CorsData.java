package com.storefront.config.data;

import java.util.List;

@Deprecated
public class CorsData {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedOriginsPatterns() {
        return null;
    }
}
