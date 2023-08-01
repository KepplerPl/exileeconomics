package com.exileeconomics.producer_consumer.producer;

import com.exileeconomics.entity.NextIdEntity;
import com.exileeconomics.http.ApiHeaderBag;
import com.exileeconomics.http.RequestHandler;
import com.exileeconomics.http.exception.HeaderNotFoundException;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.exileeconomics.http.Throttler;
import com.exileeconomics.service.NextIdService;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PublicStashTabsProducer implements NoSuppressedRunnable {
    private final BlockingQueue<String> nextIdQueue = new ArrayBlockingQueue<>(10);
    private final BlockingQueue<String> jsonResponsesQueue;
    private final Throttler throttler;
    private final RequestHandler requestHandler;
    private final NextIdService nextIdService;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;

    public PublicStashTabsProducer(
            BlockingQueue<String> jsonResponsesQueue,
            Throttler throttler,
            RequestHandler requestHandler,
            NextIdService nextIdService,
            CountDownLatch countDownLatchForCurrencyRatioInitialization
    ) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.throttler = throttler;
        this.requestHandler = requestHandler;
        this.nextIdService = nextIdService;
        this.countDownLatchForCurrencyRatioInitialization = countDownLatchForCurrencyRatioInitialization;
    }

    @Override
    public void doRun() throws IOException, InterruptedException {
        countDownLatchForCurrencyRatioInitialization.await();
        ApiHeaderBag apiHeaderBag = new ApiHeaderBag();

        if(nextIdQueue.isEmpty()) {
            setCurrentNextId();
            apiHeaderBag.setHeaders(getCurrentHeaders());
        }

        while(!nextIdQueue.isEmpty()) {
            long start = System.currentTimeMillis();

            if(!canDoRequest()){
                return;
            }

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
            try {
                nextIdFromRequest = apiHeaderBag.extractNextId();
            } catch (HeaderNotFoundException e) {
                throw new RuntimeException(e);
            }

            if(hasReachedEndOfStream(nextIdFromRequest, nextId)){
                return;
            }

            throttler.setCurrentHitCount(apiHeaderBag.getCurrentXRateLimitHits());
            if (response.getResponseCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(apiHeaderBag.getRetryAfter() + 5));
            }

            responseBody = requestHandler.getResponseAsString(response);
            jsonResponsesQueue.put(responseBody);
            saveNextId(nextIdFromRequest);

            nextIdQueue.put(nextIdFromRequest);

            long finish = System.currentTimeMillis();
            System.out.printf("Next ID: %s took %s %n", nextIdFromRequest, (finish - start) );
        }
    }

    private boolean canDoRequest() {
        if (!throttler.canDoRequest()) {
            System.out.println(Thread.currentThread().getName() + " tried to do a request but couldn't because hits are " + throttler.getCurrentCounter());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(60));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            throttler.setCurrentHitCount(2);

            return false;
        }
        throttler.decrement();

        return true;
    }

    private boolean hasReachedEndOfStream(String nextIdFromRequest, String nextId) throws InterruptedException {
        if(nextIdFromRequest.equals(nextId)) {
            nextIdQueue.put(nextId);
            throttler.setCurrentHitCount(2);
            System.out.printf("Reaching the end of the river, with id %s waiting 30 seconds...%n", nextId);
            Thread.sleep(TimeUnit.SECONDS.toMillis(30));

            return true;
        }

         return false;
    }

    private void saveNextId(String nextId) {
        NextIdEntity nextIdEntityQ = new NextIdEntity();
        nextIdEntityQ.setNextId(nextId);
        nextIdService.save(nextIdEntityQ);
    }

    private Map<String, List<String>> getCurrentHeaders() {
        try {
            HttpURLConnection request = requestHandler.getPublicStashTabs(nextIdQueue.element());
            return request.getHeaderFields();
        } catch (IOException | NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCurrentNextId() throws InterruptedException {
        NextIdEntity nextIdEntity = nextIdService.findFirstByOrderByCreatedAtDesc();
        nextIdQueue.put(nextIdEntity.getNextId());
    }
}
