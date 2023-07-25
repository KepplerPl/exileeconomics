package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.price.event.CurrencyRatioUpdateEvent;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class CurrencyRatioProducer implements NoSuppressedRunnable, ApplicationListener<CurrencyRatioUpdateEvent> {
    private final ParsableCurrency parsableCurrency;
    private final ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap;
    private CountDownLatch countDownLatch;

    public CurrencyRatioProducer(
            ParsableCurrency parsableCurrency,
            ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap
    ) {
        this.parsableCurrency = parsableCurrency;
        this.currencyRatioMap = currencyRatioMap;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void doRun() {
        for(CurrencyRatioEntity currencyRatio : parsableCurrency.getMostRecentCurrencyRatioForParsableCurrency()) {
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
