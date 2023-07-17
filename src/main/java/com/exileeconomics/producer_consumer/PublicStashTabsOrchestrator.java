package com.exileeconomics.producer_consumer;

import com.exileeconomics.AppProperties;
import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.http.ApiHeaderBag;
import com.exileeconomics.http.RequestHandler;
import com.exileeconomics.http.Throttler;
import com.exileeconomics.mapper.deserializer.PublicStashTabsDeserializer;
import com.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.exileeconomics.price.CurrencyRatioProducer;
import com.exileeconomics.price.ParsableCurrency;
import com.exileeconomics.price.SellableItemBuilder;
import com.exileeconomics.producer_consumer.consumer.PublicStashTabsConsumer;
import com.exileeconomics.producer_consumer.producer.PublicStashTabsProducer;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemDefinitionsService;
import com.exileeconomics.service.NextIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.exileeconomics.service.ItemEntityService;

import java.util.*;
import java.util.concurrent.*;

@Service
public final class PublicStashTabsOrchestrator {
    private final RequestHandler requestHandler;
    private final AppProperties appProperties;
    private final NextIdService nextIdService;
    private final ItemDefinitionsService itemDefinitionsService;
    private final ItemEntityService itemEntityService;
    private final BlockingQueue<String> jsonResponsesQueue = new ArrayBlockingQueue<>(50);
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final CurrencyRatioService currencyRatioService;
    private CurrencyRatioProducer currencyRatioProducer;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;

    public PublicStashTabsOrchestrator(
            @Autowired RequestHandler requestHandler,
            @Autowired AppProperties appProperties,
            @Autowired ItemDefinitionsService itemDefinitionsService,
            @Autowired NextIdService nextIdService,
            @Autowired ItemEntityService itemEntityService,
            @Autowired CurrencyRatioService currencyRatioService,
            @Autowired RedisTemplate<String, String> redisTemplate
            ) {
        this.requestHandler = requestHandler;
        this.appProperties = appProperties;
        this.itemEntityService = itemEntityService;
        this.itemDefinitionsService = itemDefinitionsService;
        this.nextIdService = nextIdService;
        this.currencyRatioService = currencyRatioService;
        countDownLatchForCurrencyRatioInitialization = new CountDownLatch(1);
    }

    @EventListener
    @Order(value = 2)
    public void onApplicationEvent(ApplicationReadyEvent event){
        initCurrencyRatio();

        startCurrencyRatioUpdater();
        startPublicStashProducer();
        startPublicStashConsumer();
    }

    private void initCurrencyRatio() {
        currencyRatioProducer = new CurrencyRatioProducer(ParsableCurrency.getParsableCurrencies(), currencyRatioService, itemDefinitionsService);
        currencyRatioProducer.setCountDownLatch(countDownLatchForCurrencyRatioInitialization);
    }

    private void startCurrencyRatioUpdater() {
        // this needs to be run once to get the latest currency ratio from the database
        executorService.schedule(currencyRatioProducer, 0, TimeUnit.MILLISECONDS);
    }

    private void startPublicStashConsumer() {
        PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson = new PublicStashTabsDeserializerFromJson(
                new PublicStashTabsDeserializer(
                        appProperties.getActiveLeague(),
                        new SellableItemBuilder(currencyRatioProducer)
                )
        );

        PublicStashTabsConsumer publicStashTabsConsumer = new PublicStashTabsConsumer(
                jsonResponsesQueue,
                getItemDefinitions(),
                itemEntityService,
                countDownLatchForCurrencyRatioInitialization,
                publicStashTabsDeserializerFromJson
        );
        executorService.scheduleWithFixedDelay(publicStashTabsConsumer, 10_000, 10_000, TimeUnit.MILLISECONDS);
    }

    private void startPublicStashProducer() {
        PublicStashTabsProducer publicStashTabsProducer = new PublicStashTabsProducer(
                jsonResponsesQueue,
                new Throttler(),
                requestHandler,
                new ApiHeaderBag(),
                nextIdService,
                countDownLatchForCurrencyRatioInitialization
        );
        executorService.scheduleWithFixedDelay(publicStashTabsProducer, 100, 200, TimeUnit.MILLISECONDS);
    }

    private HashMap<ItemDefinitionEnum, ItemDefinitionEntity> getItemDefinitions() {
        Iterable<ItemDefinitionEntity> itemDefinitionFromDatabase = itemDefinitionsService.findAll();
        final HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions = new HashMap<>();

        for (ItemDefinitionEntity itemDefinitionEntity : itemDefinitionFromDatabase) {
            itemDefinitions.put(ItemDefinitionEnum.fromString(itemDefinitionEntity.getName()), itemDefinitionEntity);
        }

        return itemDefinitions;
    }
}
