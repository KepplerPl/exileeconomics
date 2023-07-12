package com.example.exileeconomics.http;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * These class methods need to be synchronized if at any point I decide
 * to use it on more than one thread at once and not in a loop
 */
public class Throttler {
    private final AtomicInteger currentHitCount = new AtomicInteger(2);

    public Throttler() {
        currentHitCount.set(5);
    }

    public void decrement() {
        currentHitCount.decrementAndGet();
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
