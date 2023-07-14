package com.exileeconomics.price.event;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class CurrencyRatioUpdateEvent extends ApplicationEvent {
    private final Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap;

    public CurrencyRatioUpdateEvent(Object source, Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap) {
        super(source);
        this.currencyRatioMap = currencyRatioMap;
    }

    public Map<ItemDefinitionEnum, CurrencyRatioEntity> getCurrencyRatioMap() {
        return currencyRatioMap;
    }
}
