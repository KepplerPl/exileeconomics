package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemDefinitionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ParsableCurrency {
    private final List<ItemDefinitionEnum> parsableCurrencies = List.of(
            ItemDefinitionEnum.CHAOS_ORB,
            ItemDefinitionEnum.DIVINE_ORB
    );

    private final CurrencyRatioService currencyRatioService;
    private final ItemDefinitionsService itemDefinitionsService;

    public ParsableCurrency(
            @Autowired CurrencyRatioService currencyRatioService,
            @Autowired ItemDefinitionsService itemDefinitionsService
    ) {
        this.currencyRatioService = currencyRatioService;
        this.itemDefinitionsService = itemDefinitionsService;
    }

    public Collection<CurrencyRatioEntity> getMostRecentCurrencyRatioForParsableCurrency() {
        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsService.findAllItemDefinitionEntitiesByItemDefinitionEnums(parsableCurrencies);
        List<Long> ids = StreamSupport
                .stream(itemDefinitionEntities.spliterator(), false)
                .map(ItemDefinitionEntity::getId)
                .toList();

        return currencyRatioService.mostCurrentCurrencyRatio(ids, parsableCurrencies.size());
    }
}
