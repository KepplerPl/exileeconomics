package com.example.exileeconomics.seeder;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.entity.NextIdEntity;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;
import com.example.exileeconomics.repository.NextIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Seeder {

    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final NextIdRepository nextIdRepository;
    private final CurrencyRatioRepository currencyRatioRepository;

    public Seeder(
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository,
            @Autowired CurrencyRatioRepository currencyRatioRepository,
            @Autowired NextIdRepository nextIdRepository
    ) {
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.nextIdRepository = nextIdRepository;
        this.currencyRatioRepository = currencyRatioRepository;
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

        if(!currencyRatioRepository.findAll().iterator().hasNext()) {
            ItemDefinitionEntity divineOrb = itemDefinitionsRepository.getItemDefinitionByName(ItemDefinitionEnum.DIVINE_ORB.getName());
            CurrencyRatioEntity divineOrbToChaosRatio = new CurrencyRatioEntity();
            divineOrbToChaosRatio.setChaos(200);
            divineOrbToChaosRatio.setItemDefinitionEntity(divineOrb);
            currencyRatioRepository.save(divineOrbToChaosRatio);

            ItemDefinitionEntity awakenedSextant = itemDefinitionsRepository.getItemDefinitionByName(ItemDefinitionEnum.AWAKENED_SEXTANT.getName());
            CurrencyRatioEntity sextantToChaosRatio = new CurrencyRatioEntity();
            sextantToChaosRatio.setChaos(5);
            sextantToChaosRatio.setItemDefinitionEntity(awakenedSextant);
            currencyRatioRepository.save(sextantToChaosRatio);

            ItemDefinitionEntity chaos = itemDefinitionsRepository.getItemDefinitionByName(ItemDefinitionEnum.CHAOS_ORB.getName());
            CurrencyRatioEntity chaosToChaosRatio = new CurrencyRatioEntity();
            chaosToChaosRatio.setChaos(1);
            chaosToChaosRatio.setItemDefinitionEntity(chaos);
            currencyRatioRepository.save(chaosToChaosRatio);
        }
    }

    private void seedItemDefinitions() {
        if(!itemDefinitionsRepository.findAll().iterator().hasNext()) {
            Set<String> existingItemsAsSet = new HashSet<>();
            Iterable<ItemDefinitionEntity> existingIndexableItems = itemDefinitionsRepository.findAll();
            existingIndexableItems.forEach(item -> existingItemsAsSet.add(item.getName()));

            ItemDefinitionEnum[] items = ItemDefinitionEnum.values();

            Set<String> itemDefinitions = Arrays.stream(items).map(ItemDefinitionEnum::getName).collect(Collectors.toSet());

            itemDefinitions.removeAll(existingItemsAsSet);
            if(itemDefinitions.isEmpty()) {
                return;
            }

            for(String item:itemDefinitions) {
                ItemDefinitionEntity indexableItem = new ItemDefinitionEntity();
                indexableItem.setName(item);
                itemDefinitionsRepository.save(indexableItem);
            }
        }
    }

    private void seedNextId() {
        if(!nextIdRepository.findAll().iterator().hasNext()) {
            NextIdEntity nextIdEntity = new NextIdEntity();
            nextIdEntity.setNextId("1997310912-1992768933-1927582781-2136932361-2073806912");
            nextIdRepository.save(nextIdEntity);
        }
    }
}
