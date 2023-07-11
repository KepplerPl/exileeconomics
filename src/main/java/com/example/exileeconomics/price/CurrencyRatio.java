package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CurrencyRatio implements NoSuppressedRunnable {
    private final Set<ItemDefinitionEnum> parsableCurrencies;
    private volatile Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap;
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final CountDownLatch countDownLatch;

    public CurrencyRatioEntity getRatioFor(ItemDefinitionEnum itemDefinitionEnum) {
        return currencyRatioMap.get(itemDefinitionEnum);
    }

    public CurrencyRatio(Set<ItemDefinitionEnum> parsableCurrencies, CurrencyRatioRepository currencyRatioRepository, ItemDefinitionsRepository itemDefinitionsRepository, CountDownLatch countDownLatch) {
        this.parsableCurrencies = parsableCurrencies;
        this.currencyRatioRepository = currencyRatioRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void doRun() {
        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsRepository.findAllByNameIn(
                parsableCurrencies
                        .stream()
                        .map(ItemDefinitionEnum::getName)
                        .collect(Collectors.toSet())
        );

        int limit = parsableCurrencies.size();
        List<Long> longs = StreamSupport
                .stream(itemDefinitionEntities.spliterator(), false)
                .map(ItemDefinitionEntity::getId)
                .toList();

        Collection<CurrencyRatioEntity> currencyRatioEntities = currencyRatioRepository.mostCurrentCurrencyRatio(longs, limit);

        Map<ItemDefinitionEnum, CurrencyRatioEntity> modifableMap = new HashMap<>();

        for(CurrencyRatioEntity currencyRatio : currencyRatioEntities) {
            modifableMap.put(ItemDefinitionEnum.fromString(currencyRatio.getItemDefinitionEntity().getName().toLowerCase()), currencyRatio);
        }

        currencyRatioMap = Collections.unmodifiableMap(modifableMap);

        countDownLatch.countDown();
    }
}
