package com.exileeconomics.seeder;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.entity.NextIdEntity;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemDefinitionsService;
import com.exileeconomics.service.NextIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Seeder {
    private final ItemDefinitionsService itemDefinitionsService;
    private final NextIdService nextIdService;
    private final CurrencyRatioService currencyRatioService;

    public Seeder(
            @Autowired ItemDefinitionsService itemDefinitionsService,
            @Autowired CurrencyRatioService currencyRatioService,
            @Autowired NextIdService nextIdService
    ) {
        this.itemDefinitionsService = itemDefinitionsService;
        this.nextIdService = nextIdService;
        this.currencyRatioService = currencyRatioService;
    }

    @EventListener
    @Order(value = 1)
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        seedNextId();
        seedItemDefinitions();
        seedCurrencyRatio();
    }

    private void seedCurrencyRatio() {

        if(!currencyRatioService.findAll().iterator().hasNext()) {
            ItemDefinitionEntity divineOrb = itemDefinitionsService.getItemDefinitionEntityByItemDefinitionEnum(ItemDefinitionEnum.DIVINE_ORB);
            CurrencyRatioEntity divineOrbToChaosRatio = new CurrencyRatioEntity();
            divineOrbToChaosRatio.setChaos(new BigDecimal(186));
            divineOrbToChaosRatio.setItemDefinitionEntity(divineOrb);
            currencyRatioService.save(divineOrbToChaosRatio);

            ItemDefinitionEntity awakenedSextant = itemDefinitionsService.getItemDefinitionEntityByItemDefinitionEnum(ItemDefinitionEnum.AWAKENED_SEXTANT);
            CurrencyRatioEntity sextantToChaosRatio = new CurrencyRatioEntity();
            sextantToChaosRatio.setChaos(new BigDecimal(5));
            sextantToChaosRatio.setItemDefinitionEntity(awakenedSextant);
            currencyRatioService.save(sextantToChaosRatio);

            ItemDefinitionEntity chaos = itemDefinitionsService.getItemDefinitionEntityByItemDefinitionEnum(ItemDefinitionEnum.CHAOS_ORB);
            CurrencyRatioEntity chaosToChaosRatio = new CurrencyRatioEntity();
            chaosToChaosRatio.setChaos(new BigDecimal(1));
            chaosToChaosRatio.setItemDefinitionEntity(chaos);
            currencyRatioService.save(chaosToChaosRatio);
        }
    }

    private void seedItemDefinitions() {
        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsService.findAll();
        long size = itemDefinitionEntities.spliterator().getExactSizeIfKnown();

        if(size != ItemDefinitionEnum.values().length) {
            Set<String> existingItemsAsSet = new HashSet<>();
            itemDefinitionEntities.forEach(item -> existingItemsAsSet.add(item.getName()));

            ItemDefinitionEnum[] items = ItemDefinitionEnum.values();

            Set<String> itemDefinitions = Arrays.stream(items).map(ItemDefinitionEnum::getName).collect(Collectors.toSet());

            itemDefinitions.removeAll(existingItemsAsSet);
            if(itemDefinitions.isEmpty()) {
                return;
            }

            for(String item:itemDefinitions) {
                ItemDefinitionEntity indexableItem = new ItemDefinitionEntity();
                indexableItem.setName(item);
                itemDefinitionsService.save(indexableItem);
            }
        }
    }

    private void seedNextId() {
        if(!nextIdService.findAll().iterator().hasNext()) {
            NextIdEntity nextIdEntity = new NextIdEntity();
//            nextIdEntity.setNextId("2004283927-2001025847-1934275331-2145533334-2080672660");
            nextIdEntity.setNextId("1997237783-1992671469-1927505622-2136845092-2073718601");
            nextIdService.save(nextIdEntity);
        }
    }
}
