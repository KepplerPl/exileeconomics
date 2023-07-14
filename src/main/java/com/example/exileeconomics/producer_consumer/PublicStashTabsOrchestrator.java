package com.example.exileeconomics.producer_consumer;

import com.example.exileeconomics.Properties;
import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.http.ApiHeaderBag;
import com.example.exileeconomics.http.RequestHandler;
import com.example.exileeconomics.http.Throttler;
import com.example.exileeconomics.mapper.deserializer.PublicStashTabsDeserializer;
import com.example.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.example.exileeconomics.price.CurrencyRatioProducer;
import com.example.exileeconomics.price.SellableItemBuilder;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public final class PublicStashTabsOrchestrator {
    private final RequestHandler requestHandler;
    private final Properties properties;
    private final NextIdRepository nextIdRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final BlockingQueue<String> jsonResponsesQueue = new ArrayBlockingQueue<>(50);
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final CurrencyRatioRepository currencyRatioRepository;
    private CurrencyRatioProducer currencyRatioProducer;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;

    public PublicStashTabsOrchestrator(
            @Autowired RequestHandler requestHandler,
            @Autowired Properties properties,
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository,
            @Autowired NextIdRepository nextIdRepository,
            @Autowired ItemEntityRepository itemEntityRepository,
            @Autowired CurrencyRatioRepository currencyRatioRepository,
            @Autowired RedisTemplate<String, String> redisTemplate
            ) {
        this.requestHandler = requestHandler;
        this.properties = properties;
        this.itemEntityRepository = itemEntityRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.nextIdRepository = nextIdRepository;
        this.currencyRatioRepository = currencyRatioRepository;
        countDownLatchForCurrencyRatioInitialization = new CountDownLatch(1);
    }

    @EventListener
    @Order(value = 2)
    public void onApplicationEvent(ApplicationReadyEvent event){
//        initCurrencyRatio();
//
//        startCurrencyRatioUpdater();
//        startPublicStashProducer();
//        startPublicStashConsumer();
    }

    private void initCurrencyRatio() {
        Set<ItemDefinitionEnum> parsableCurrencies = new HashSet<>(List.of(
                ItemDefinitionEnum.CHAOS_ORB,
                ItemDefinitionEnum.DIVINE_ORB,
                ItemDefinitionEnum.AWAKENED_SEXTANT)
        );
        currencyRatioProducer = new CurrencyRatioProducer(parsableCurrencies, currencyRatioRepository, itemDefinitionsRepository);
        currencyRatioProducer.setCountDownLatch(countDownLatchForCurrencyRatioInitialization);
    }

    private void startCurrencyRatioUpdater() {
        // this needs to be run once to get the latest currency ratio from the database
        executorService.schedule(currencyRatioProducer, 0, TimeUnit.MILLISECONDS);
    }

    private void startPublicStashConsumer() {
        PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson = new PublicStashTabsDeserializerFromJson(
                new PublicStashTabsDeserializer(
                        properties.getActiveLeague(),
                        new SellableItemBuilder(currencyRatioProducer)
                )
        );

        PublicStashTabsConsumer publicStashTabsConsumer = new PublicStashTabsConsumer(
                jsonResponsesQueue,
                getItemDefinitions(),
                itemEntityRepository,
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
                nextIdRepository,
                countDownLatchForCurrencyRatioInitialization
        );
        executorService.scheduleWithFixedDelay(publicStashTabsProducer, 100, 200, TimeUnit.MILLISECONDS);
    }

    private HashMap<ItemDefinitionEnum, ItemDefinitionEntity> getItemDefinitions() {
        Iterable<ItemDefinitionEntity> itemDefinitionFromDatabase = itemDefinitionsRepository.findAll();
        final HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions = new HashMap<>();

        for (ItemDefinitionEntity itemDefinitionEntity : itemDefinitionFromDatabase) {
            itemDefinitions.put(ItemDefinitionEnum.fromString(itemDefinitionEntity.getName()), itemDefinitionEntity);
        }

        return itemDefinitions;
    }
}
