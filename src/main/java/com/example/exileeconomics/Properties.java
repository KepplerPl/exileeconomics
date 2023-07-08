package com.example.exileeconomics;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class Properties {
    @Value("${exileeconomics.bearer_token}")
    private String bearerToken;
    @Value("${exileeconomics.api.host}")
    private String host;
    @Value("${exileeconomics.api.schema}")
    private String schema;
    @Value("${exileeconomics.api.scope.service.psapi}")
    private String psapi;
    @Value("${exileeconomics.api.user_agent}")
    private String userAgent;
    @Value("${exileeconomics.active_league}")
    private String activeLeague;

    public String getActiveLeague() {
        return activeLeague;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getHost() {
        return host;
    }

    public String getSchema() {
        return schema;
    }

    public String getPsapi() {
        return psapi;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
