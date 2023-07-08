package com.example.exileeconomics.producer_consumer;

import java.io.IOException;

// https://stackoverflow.com/questions/6894595/scheduledexecutorservice-exception-handling
@FunctionalInterface
public interface NoSuppressedRunnable extends Runnable {

    @Override
    default void run() {
        try {
            doRun();
        } catch (Exception e) {
            // should probably log this
            System.out.println(e.getMessage());
        }
    }

    void doRun() throws IOException, InterruptedException;
}