package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.event.CurrencyRatioUpdateEvent;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemDefinitionsService;
import org.springframework.context.ApplicationListener;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

public class CurrencyRatioProducer implements NoSuppressedRunnable, ApplicationListener<CurrencyRatioUpdateEvent> {
    private final Set<ItemDefinitionEnum> parsableCurrencies;
    private final ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap;
    private final CurrencyRatioService currencyRatioService;
    private final ItemDefinitionsService itemDefinitionsService;
    private CountDownLatch countDownLatch;

    public CurrencyRatioProducer(
            Set<ItemDefinitionEnum> parsableCurrencies,
            ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap,
            CurrencyRatioService currencyRatioService,
            ItemDefinitionsService itemDefinitionsService
    ) {
        this.parsableCurrencies = parsableCurrencies;
        this.currencyRatioMap = currencyRatioMap;
        this.currencyRatioService = currencyRatioService;
        this.itemDefinitionsService = itemDefinitionsService;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void doRun() {
        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsService.findAllItemDefinitionEntitiesByItemDefinitionEnums(parsableCurrencies);

        int limit = parsableCurrencies.size();
        List<Long> longs = StreamSupport
                .stream(itemDefinitionEntities.spliterator(), false)
                .map(ItemDefinitionEntity::getId)
                .toList();

        Collection<CurrencyRatioEntity> currencyRatioEntities = currencyRatioService.mostCurrentCurrencyRatio(longs, limit);

        for(CurrencyRatioEntity currencyRatio : currencyRatioEntities) {
            currencyRatioMap.put(ItemDefinitionEnum.fromString(currencyRatio.getItemDefinitionEntity().getName().toLowerCase()), currencyRatio);
        }

        countDownLatch.countDown();
    }

    @Override
    public void onApplicationEvent(CurrencyRatioUpdateEvent event) {
        currencyRatioMap.clear();
        currencyRatioMap.putAll(event.getCurrencyRatioMap());
    }
}
