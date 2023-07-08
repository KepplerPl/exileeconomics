package com.example.exileeconomics.http;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service()
public class Throttler {
    private final AtomicInteger currentHitCount = new AtomicInteger(5);

    public Throttler() {
        currentHitCount.set(5);
    }

    public synchronized int decrement() {
        return currentHitCount.decrementAndGet();
    }

    public synchronized boolean canDoRequest() {
        return currentHitCount.get() > 0;
    }

    public synchronized int getCurrentCounter() {
        return currentHitCount.get();
    }

    public synchronized void setCurrentHitCount(int currentHitCount) {
        this.currentHitCount.set(currentHitCount);
    }

}
