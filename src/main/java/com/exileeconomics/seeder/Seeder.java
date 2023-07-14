package com.exileeconomics.seeder;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.entity.NextIdEntity;
import com.exileeconomics.repository.CurrencyRatioRepository;
import com.exileeconomics.repository.ItemDefinitionsRepository;
import com.exileeconomics.repository.NextIdRepository;
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
            divineOrbToChaosRatio.setChaos(new BigDecimal(186));
            divineOrbToChaosRatio.setItemDefinitionEntity(divineOrb);
            currencyRatioRepository.save(divineOrbToChaosRatio);

            ItemDefinitionEntity awakenedSextant = itemDefinitionsRepository.getItemDefinitionByName(ItemDefinitionEnum.AWAKENED_SEXTANT.getName());
            CurrencyRatioEntity sextantToChaosRatio = new CurrencyRatioEntity();
            sextantToChaosRatio.setChaos(new BigDecimal(5));
            sextantToChaosRatio.setItemDefinitionEntity(awakenedSextant);
            currencyRatioRepository.save(sextantToChaosRatio);

            ItemDefinitionEntity chaos = itemDefinitionsRepository.getItemDefinitionByName(ItemDefinitionEnum.CHAOS_ORB.getName());
            CurrencyRatioEntity chaosToChaosRatio = new CurrencyRatioEntity();
            chaosToChaosRatio.setChaos(new BigDecimal(1));
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
            nextIdEntity.setNextId("2000728804-1996698714-1930863570-2140969949-2077177458");
            nextIdRepository.save(nextIdEntity);
        }
    }
}
