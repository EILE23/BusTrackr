package com.bustrackr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bus.api")
public class BusApiProperties {
    
    private String baseUrl;
    private String serviceKey;
    private int timeout = 5000;
    
    // Getters and Setters
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getServiceKey() {
        return serviceKey;
    }
    
    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}