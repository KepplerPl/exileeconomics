package com.example.exileeconomics.producer_consumer;

import com.example.exileeconomics.Properties;
import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.entity.NextIdEntity;
import com.example.exileeconomics.http.ApiHeaderBag;
import com.example.exileeconomics.http.RequestHandler;
import com.example.exileeconomics.http.Throttler;
import com.example.exileeconomics.mapper.deserializer.PublicStashTabsDeserializer;
import com.example.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.example.exileeconomics.price.CurrencyRatio;
import com.example.exileeconomics.price.PriceParser;
import com.example.exileeconomics.producer_consumer.consumer.PublicStashTabsConsumer;
import com.example.exileeconomics.producer_consumer.producer.PublicStashTabsProducer;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
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
    private final HashMap<String, ItemDefinitionEntity> itemDefinitions = new HashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    private final ApiHeaderBag apiHeaderBag;
    private final Throttler throttler;
    private final CurrencyRatioRepository currencyRatioRepository;
    private CurrencyRatio currencyRatio;
    private final CountDownLatch countDownLatch;

    public PublicStashTabsOrchestrator(
            @Autowired RequestHandler requestHandler,
            @Autowired Properties properties,
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository,
            @Autowired NextIdRepository nextIdRepository,
            @Autowired ItemEntityRepository itemEntityRepository,
            @Autowired ApiHeaderBag apiHeaderBag,
            @Autowired Throttler throttler,
            @Autowired CurrencyRatioRepository currencyRatioRepository
    ) {
        this.requestHandler = requestHandler;
        this.properties = properties;
        this.itemEntityRepository = itemEntityRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.nextIdRepository = nextIdRepository;
        this.apiHeaderBag = apiHeaderBag;
        this.throttler = throttler;
        this.currencyRatioRepository = currencyRatioRepository;

        countDownLatch = new CountDownLatch(1);

        init();
    }

    private void init() {
        Set<String> parsableCurrencies = new HashSet<>(List.of(
                ItemDefinitionEnum.CHAOS_ORB.getName(),
                ItemDefinitionEnum.DIVINE_ORB.getName(),
                ItemDefinitionEnum.AWAKENED_SEXTANT.getName())
        );
        currencyRatio = new CurrencyRatio(parsableCurrencies, currencyRatioRepository, itemDefinitionsRepository, countDownLatch);
    }

    @EventListener
    @Order(value = 2)
    public void onApplicationEvent(ApplicationReadyEvent event) throws InterruptedException {

        setCurrentNextId();
        setCurrentItemDefinitions();
        setInitialHeaders();
        startPublicStashProducer();
        startPublicStashConsumer();
        startCurrencyRatioUpdater();
    }

    private void startCurrencyRatioUpdater() {
        executorService.schedule(currencyRatio, 0, TimeUnit.MILLISECONDS); // run it once to get it going
        executorService.scheduleWithFixedDelay(currencyRatio, 0, 6, TimeUnit.HOURS);
    }

    private void startPublicStashConsumer() {

        PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson = new PublicStashTabsDeserializerFromJson(
                new PublicStashTabsDeserializer(
                        properties.getActiveLeague(),
                        new PriceParser(currencyRatio)
                )
        );

        PublicStashTabsConsumer publicStashTabsConsumer = new PublicStashTabsConsumer(
                jsonResponsesQueue,
                itemDefinitions,
                itemEntityRepository,
                countDownLatch,
                publicStashTabsDeserializerFromJson
        );
        executorService.scheduleWithFixedDelay(publicStashTabsConsumer, 10_000, 10_000, TimeUnit.MILLISECONDS);
    }

    private void startPublicStashProducer() {
        PublicStashTabsProducer publicStashTabsProducer = new PublicStashTabsProducer(
                nextIdQueue,
                jsonResponsesQueue,
                throttler,
                requestHandler,
                apiHeaderBag,
                nextIdRepository,
                countDownLatch
        );
        executorService.scheduleWithFixedDelay(publicStashTabsProducer, 100, 10, TimeUnit.MILLISECONDS);
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
        Iterable<ItemDefinitionEntity> itemDefinitionFromDatabase = itemDefinitionsRepository.findAll();
        itemDefinitionFromDatabase.forEach(itemDef -> itemDefinitions.put(itemDef.getName(), itemDef));
    }

    private void setCurrentNextId() throws InterruptedException {
        Collection<NextIdEntity> nextIdEntities = nextIdRepository.mostCurrentNextId();
        if (nextIdEntities.stream().findFirst().isPresent()) {
            NextIdEntity nextIdEntity = nextIdEntities.stream().findFirst().get();
            nextIdQueue.put(nextIdEntity.getNextId());
        }
    }
}
