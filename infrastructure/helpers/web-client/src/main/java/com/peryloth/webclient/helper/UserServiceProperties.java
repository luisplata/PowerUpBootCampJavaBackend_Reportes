package com.peryloth.webclient.helper;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user-service")
public class UserServiceProperties {

    private String baseUrl;
    private String secretKey;

    @PostConstruct
    public void init() {
        System.out.println("🔑 BaseURL=" + baseUrl + " SecretKey=" + (secretKey != null ? "OK" : "NULL"));
    }

    // getters & setters
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
