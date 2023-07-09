package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

public class CurrencyRatio implements NoSuppressedRunnable {
    private final Set<String> parsableCurrencies;
    private volatile Map<ItemDefinitionEnum, Integer> currencyRatioMap = new HashMap<>();
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final CountDownLatch countDownLatch;

    public Integer getRatioFor(ItemDefinitionEnum itemDefinitionEnum) {
        return currencyRatioMap.get(itemDefinitionEnum);
    }

    public CurrencyRatio(Set<String> parsableCurrencies, CurrencyRatioRepository currencyRatioRepository, ItemDefinitionsRepository itemDefinitionsRepository, CountDownLatch countDownLatch) {
        this.parsableCurrencies = parsableCurrencies;
        this.currencyRatioRepository = currencyRatioRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void doRun() {
        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsRepository.findAllByNameIn(parsableCurrencies);

        int limit = parsableCurrencies.size();

        List<Long> longs = StreamSupport
                .stream(itemDefinitionEntities.spliterator(), false)
                .map(ItemDefinitionEntity::getId)
                .toList();

        Collection<CurrencyRatioEntity> currencyRatioEntities = currencyRatioRepository.mostCurrentCurrencyRatio(longs, limit);

        for(CurrencyRatioEntity currencyRatio : currencyRatioEntities) {
            currencyRatioMap.put(ItemDefinitionEnum.fromString(currencyRatio.getItemDefinition().getName().toLowerCase()), currencyRatio.getChaos());
        }

        // one chaos is always equal to itself
        currencyRatioMap.put(ItemDefinitionEnum.CHAOS_ORB, 1);

        countDownLatch.countDown();
    }
}
