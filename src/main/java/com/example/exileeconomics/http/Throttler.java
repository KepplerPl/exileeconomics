package com.example.exileeconomics.http;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

public class Throttler {
    private final AtomicInteger currentHitCount = new AtomicInteger(5);

    public Throttler() {
        currentHitCount.set(5);
    }

    public int decrement() {
        return currentHitCount.decrementAndGet();
    }

    public boolean canDoRequest() {
        return currentHitCount.get() > 0;
    }

    public int getCurrentCounter() {
        return currentHitCount.get();
    }

    public void setCurrentHitCount(int currentHitCount) {
        this.currentHitCount.set(currentHitCount);
    }

}
