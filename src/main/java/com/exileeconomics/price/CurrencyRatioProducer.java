package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.event.CurrencyRatioUpdateEvent;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;
import com.exileeconomics.repository.CurrencyRatioRepository;
import com.exileeconomics.repository.ItemDefinitionsRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CurrencyRatioProducer implements NoSuppressedRunnable, ApplicationListener<CurrencyRatioUpdateEvent> {
    private final Set<ItemDefinitionEnum> parsableCurrencies;
    private final Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap = new HashMap<>();
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private CountDownLatch countDownLatch;

    public CurrencyRatioEntity getRatioFor(ItemDefinitionEnum itemDefinitionEnum) {
        synchronized (currencyRatioMap) {
            return currencyRatioMap.get(itemDefinitionEnum);
        }
    }

    public CurrencyRatioProducer(Set<ItemDefinitionEnum> parsableCurrencies, CurrencyRatioRepository currencyRatioRepository, ItemDefinitionsRepository itemDefinitionsRepository) {
        this.parsableCurrencies = parsableCurrencies;
        this.currencyRatioRepository = currencyRatioRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
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

        for(CurrencyRatioEntity currencyRatio : currencyRatioEntities) {
            currencyRatioMap.put(ItemDefinitionEnum.fromString(currencyRatio.getItemDefinitionEntity().getName().toLowerCase()), currencyRatio);
        }

        countDownLatch.countDown();
    }

    @Override
    public void onApplicationEvent(CurrencyRatioUpdateEvent event) {
        synchronized (currencyRatioMap) {
            currencyRatioMap.clear();
            currencyRatioMap.putAll(event.getCurrencyRatioMap());
        }
    }
}
