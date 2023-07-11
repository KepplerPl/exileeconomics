package com.example.exileeconomics.producer_consumer.producer;

import com.example.exileeconomics.entity.NextIdEntity;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class PublicStashTabsProducer implements NoSuppressedRunnable {
    private final BlockingQueue<String> nextIdQueue;
    private final BlockingQueue<String> jsonResponsesQueue;
    private final Throttler throttler;
    private final RequestHandler requestHandler;
    private final ApiHeaderBag apiHeaderBag;
    private final NextIdRepository nextIdRepository;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;

    public PublicStashTabsProducer(
            BlockingQueue<String> nextIdQueue,
            BlockingQueue<String> jsonResponsesQueue,
            Throttler throttler,
            RequestHandler requestHandler,
            ApiHeaderBag apiHeaderBag,
            NextIdRepository nextIdRepository,
            CountDownLatch countDownLatchForCurrencyRatioInitialization
    ) {
        this.nextIdQueue = nextIdQueue;
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.throttler = throttler;
        this.requestHandler = requestHandler;
        this.apiHeaderBag = apiHeaderBag;
        this.nextIdRepository = nextIdRepository;
        this.countDownLatchForCurrencyRatioInitialization = countDownLatchForCurrencyRatioInitialization;
    }

    @Override
    public void doRun() throws IOException, InterruptedException {
        countDownLatchForCurrencyRatioInitialization.await();
        long start = System.currentTimeMillis();

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
        nextIdFromRequest = apiHeaderBag.extractNextId();

        if(nextIdFromRequest.equals(nextId)) {
            nextIdQueue.put(nextId);
            throttler.setCurrentHitCount(2);
            System.out.printf("Reaching the end of the river, with id %s waiting 30 seconds...%n", nextId);
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
            return;
        }

        throttler.setCurrentHitCount(apiHeaderBag.getCurrentXRateLimitHits());
        if (response.getResponseCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
            Thread.sleep(TimeUnit.SECONDS.toMillis(apiHeaderBag.getRetryAfter() + 5));
        }

        responseBody = requestHandler.getResponseAsString(response);

        nextIdQueue.put(nextIdFromRequest);
        jsonResponsesQueue.put(responseBody);

        saveNextId(nextIdFromRequest);
        long finish = System.currentTimeMillis();
        System.out.println("Next ID: " + nextIdFromRequest + " took " + (finish - start));
    }

    private void saveNextId(String nextId) {
        NextIdEntity nextIdEntityQ = new NextIdEntity();
        nextIdEntityQ.setNextId(nextId);
        nextIdRepository.save(nextIdEntityQ);
    }
}
