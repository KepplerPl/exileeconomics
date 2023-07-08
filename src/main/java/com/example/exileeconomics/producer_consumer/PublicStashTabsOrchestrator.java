package com.example.exileeconomics.producer_consumer;

import com.example.exileeconomics.Properties;
import com.example.exileeconomics.entity.ItemDefinition;
import com.example.exileeconomics.entity.NextId;
import com.example.exileeconomics.http.ApiHeaderBag;
import com.example.exileeconomics.http.RequestHandler;
import com.example.exileeconomics.http.Throttler;
import com.example.exileeconomics.producer_consumer.consumer.PublicStashTabsConsumer;
import com.example.exileeconomics.producer_consumer.producer.PublicStashTabsProducer;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;
import com.example.exileeconomics.repository.ItemEntityRepository;
import com.example.exileeconomics.repository.NextIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.*;

@Service
public class PublicStashTabsOrchestrator {
    private final RequestHandler requestHandler;
    private final Properties properties;
    private final NextIdRepository nextIdRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final BlockingQueue<String> nextIdQueue = new ArrayBlockingQueue<>(10);
    private final BlockingQueue<String> jsonResponsesQueue = new ArrayBlockingQueue<>(100);
    private final HashMap<String, ItemDefinition> itemDefinitions = new HashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final ApiHeaderBag apiHeaderBag;
    private final Throttler throttler;
    private final int CONSUMER_MILLISECONDS_DELAY = 10_000;
    private final int PRODUCER_MILLISECONDS_DELAY = 1000;

    public PublicStashTabsOrchestrator(
            @Autowired RequestHandler requestHandler,
            @Autowired Properties properties,
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository,
            @Autowired NextIdRepository nextIdRepository,
            @Autowired ItemEntityRepository itemEntityRepository,
            @Autowired ApiHeaderBag apiHeaderBag,
            @Autowired Throttler throttler
    ) {
        this.requestHandler = requestHandler;
        this.properties = properties;
        this.itemEntityRepository = itemEntityRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.nextIdRepository = nextIdRepository;
        this.apiHeaderBag = apiHeaderBag;
        this.throttler = throttler;
    }

    @EventListener
    @Order(value = 2)
    public void onApplicationEvent(ApplicationReadyEvent event) throws InterruptedException {
        setCurrentNextId();
        setCurrentItemDefinitions();
        setInitialHeaders();

        PublicStashTabsProducer publicStashTabsProducer = new PublicStashTabsProducer(
                nextIdQueue,
                jsonResponsesQueue,
                throttler,
                requestHandler,
                apiHeaderBag,
                nextIdRepository
        );
        executorService.scheduleWithFixedDelay(publicStashTabsProducer, 20, PRODUCER_MILLISECONDS_DELAY, TimeUnit.MILLISECONDS);

        PublicStashTabsConsumer publicStashTabsConsumer = new PublicStashTabsConsumer(
                jsonResponsesQueue,
                properties,
                itemDefinitions,
                itemEntityRepository
        );
        executorService.scheduleWithFixedDelay(publicStashTabsConsumer, 2000, CONSUMER_MILLISECONDS_DELAY, TimeUnit.MILLISECONDS);
    }

    private void setInitialHeaders() {
        try {
            String nextId = nextIdQueue.peek();
            HttpURLConnection request = requestHandler.getPublicStashTabs(nextId);
            Map<String, List<String>> headers = request.getHeaderFields();
            apiHeaderBag.setHeaders(headers);
        } catch (IOException e) {
            System.out.println("function setInitialHeaders tried to poll nextId but gave up after waiting 10 seconds");
            throw new RuntimeException(e);
        }
    }

    private void setCurrentItemDefinitions() {
        Iterable<ItemDefinition> itemDefinitionFromDatabase = itemDefinitionsRepository.findAll();
        itemDefinitionFromDatabase.forEach(itemDef -> itemDefinitions.put(itemDef.getName(), itemDef));
    }

    private void setCurrentNextId() throws InterruptedException {
        Collection<NextId> nextIds = nextIdRepository.mostCurrentNextId();
        if (nextIds.stream().findFirst().isPresent()) {
            NextId nextId = nextIds.stream().findFirst().get();
            nextIdQueue.put(nextId.getNextId());
        }
    }
}
