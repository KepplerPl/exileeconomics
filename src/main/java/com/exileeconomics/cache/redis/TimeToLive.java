package com.exileeconomics.cache.redis;

import java.util.concurrent.TimeUnit;

public class TimeToLive {

    private final long ttl;
    private final TimeUnit timeUnit;

    public TimeToLive(long ttl, TimeUnit timeUnit) {
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
