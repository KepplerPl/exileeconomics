package com.example.exileeconomics.http;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiHeaderBag {
    private volatile Map<String, List<String>> headers = new HashMap<>();

    public synchronized void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String extractNextId() {
        return headers.get("X-Next-Change-Id").get(0);
    }

    public synchronized String getHeaderValue(String value) {
        if(headers.containsKey(value) && !headers.get(value).isEmpty()) {
            return headers.get(value).get(0);
        }

        return "";
    }

    public int getRetryAfter() {
        if(!headers.containsKey("Retry-After")) {
            return 0;
        }

        return Integer.parseInt(getHeaderValue("Retry-After"));
    }

    private String[] getCurrentXRateLimit() {
        String ruleName = getHeaderValue("X-Rate-Limit-Rules");
        return getHeaderValue(String.format("X-Rate-Limit-%s-State", ruleName)).trim().split(":");
    }

    private String[] getXRateLimit() {
        String ruleName = getHeaderValue("X-Rate-Limit-Rules");
        return getHeaderValue(String.format("X-Rate-Limit-%s", ruleName)).trim().split(":");
    }

    public int getCurrentXRateLimitHits() {
        return Integer.parseInt(getCurrentXRateLimit()[0]);
    }


    public int getCurrentXRateLimitTestedPeriod() {
        return Integer.parseInt(getCurrentXRateLimit()[1]);
    }

    public int getXRateLimitMaximumHits() {
        return Integer.parseInt(getXRateLimit()[0]);
    }

    public int getXRateLimitTestedPeriod() {
        return Integer.parseInt(getXRateLimit()[1]);
    }
}
