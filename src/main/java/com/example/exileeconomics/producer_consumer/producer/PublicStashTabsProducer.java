package com.example.exileeconomics.producer_consumer.producer;

import com.example.exileeconomics.entity.NextId;
import com.example.exileeconomics.http.ApiHeaderBag;
import com.example.exileeconomics.http.RequestHandler;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.example.exileeconomics.http.Throttler;
import com.example.exileeconomics.repository.NextIdRepository;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class PublicStashTabsProducer implements NoSuppressedRunnable {
    private final BlockingQueue<String> nextIdQueue;
    private final BlockingQueue<String> jsonResponsesQueue;
    private final Throttler throttler;
    private final RequestHandler requestHandler;
    private final ApiHeaderBag apiHeaderBag;
    private final NextIdRepository nextIdRepository;

    public PublicStashTabsProducer(BlockingQueue<String> nextIdQueue, BlockingQueue<String> jsonResponsesQueue, Throttler throttler, RequestHandler requestHandler, ApiHeaderBag apiHeaderBag, NextIdRepository nextIdRepository) {
        this.nextIdQueue = nextIdQueue;
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.throttler = throttler;
        this.requestHandler = requestHandler;
        this.apiHeaderBag = apiHeaderBag;
        this.nextIdRepository = nextIdRepository;
    }

    @Override
    public void doRun() throws IOException, InterruptedException {
        if (!throttler.canDoRequest()) {
            System.out.println(Thread.currentThread().getName() + " tried to do a request but couldn't because hits are " + throttler.getCurrentCounter());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(apiHeaderBag.getCurrentXRateLimitTestedPeriod()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        throttler.decrement();

        String responseBody;
        HttpURLConnection response;
        Map<String, List<String>> headers;
        String nextIdFromRequest;

        String nextId = nextIdQueue.poll(3, TimeUnit.SECONDS);
        if (nextId == null) {
            return;
        }

        response = requestHandler.getPublicStashTabs(nextId);
        headers = response.getHeaderFields();
        apiHeaderBag.setHeaders(headers);
        throttler.setCurrentHitCount(apiHeaderBag.getCurrentXRateLimitHits());
        if (response.getResponseCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(apiHeaderBag.getRetryAfter() + 2));
            return;
        }

        responseBody = requestHandler.getResponseAsString(response);

        nextIdFromRequest = apiHeaderBag.extractNextId();
        nextIdQueue.put(nextIdFromRequest);
        jsonResponsesQueue.put(responseBody);

        saveNextId(nextIdFromRequest);

        System.out.println("Next ID: " + nextIdFromRequest);
    }

    private void saveNextId(String nextId) {
        NextId nextIdQ = new NextId();
        nextIdQ.setNextId(nextId);
        nextIdRepository.save(nextIdQ);
    }
}
