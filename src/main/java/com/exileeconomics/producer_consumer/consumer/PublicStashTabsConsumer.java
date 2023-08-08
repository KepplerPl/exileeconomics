package com.exileeconomics.producer_consumer.consumer;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemEntityMod;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;

import com.exileeconomics.entity.ItemEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.mapper.ItemDTO;
import com.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.exileeconomics.service.ItemDefinitionsService;
import com.exileeconomics.service.ItemEntityModService;
import com.exileeconomics.service.ItemEntityService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class PublicStashTabsConsumer implements NoSuppressedRunnable {
    private final BlockingQueue<String> jsonResponsesQueue;
    private final Map<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions;
    private final ItemEntityService itemEntityService;
    private final ItemEntityModService itemEntityModService;
    private final ConcurrentMap<String, ItemEntityMod> existingItemEntityMods;
    private final ItemDefinitionsService itemDefinitionsService;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;
    private final PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson;
    private final List<ItemDefinitionEntity> itemDefinitionEntities = new ArrayList<>(100);
    private final Set<ItemDefinitionEnum> itemDefinitionsIcons = new HashSet<>() ;

    public PublicStashTabsConsumer(
            BlockingQueue<String> jsonResponsesQueue,
            Map<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions,
            ItemEntityService itemEntityService,
            CountDownLatch countDownLatchForCurrencyRatioInitialization,
            PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson,
            ItemDefinitionsService itemDefinitionsService,
            ConcurrentMap<String, ItemEntityMod> existingItemEntityMods,
            ItemEntityModService itemEntityModService
    ) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.itemDefinitions = itemDefinitions;
        this.itemEntityService = itemEntityService;
        this.countDownLatchForCurrencyRatioInitialization = countDownLatchForCurrencyRatioInitialization;
        this.publicStashTabsDeserializerFromJson = publicStashTabsDeserializerFromJson;
        this.itemDefinitionsService = itemDefinitionsService;
        this.existingItemEntityMods = existingItemEntityMods;
        this.itemEntityModService = itemEntityModService;

        itemDefinitionsIcons.addAll(itemDefinitions.keySet());
    }

    @Override
    public void doRun() throws InterruptedException {
        countDownLatchForCurrencyRatioInitialization.await();

        if(jsonResponsesQueue.isEmpty()) {
            System.out.println("Nothing to consume, returning...");
            return;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
                List.class,
                publicStashTabsDeserializerFromJson
        );
        Gson gson = builder.create();

        while(jsonResponsesQueue.iterator().hasNext()) {
            saveItems(
                    // this is unchecked returned type unfortunately
                    gson.fromJson(jsonResponsesQueue.take(), List.class)
            );
        }

        System.out.printf("Queue now has %d items%n", jsonResponsesQueue.size());
    }

    private void saveItems(List<ItemDTO> items) {
        if (!items.isEmpty()) {
            final List<ItemEntity> itemEntityList = new ArrayList<>(100);

            for (ItemDTO itemDTO : items) {
                ItemDefinitionEntity itemDefinition = itemDefinitions.get(itemDTO.getItem());
                if(itemDefinition.getIcon() == null && itemDefinitionsIcons.contains(itemDTO.getItem())) {
                    itemDefinition.setIcon(itemDTO.getIcon());
                    itemDefinitionEntities.add(itemDefinition);
                    itemDefinitionsIcons.remove(itemDTO.getItem());
                }

                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setPrice(itemDTO.getPrice());
                itemEntity.setSoldQuantity(itemDTO.getSoldQuantity());
                itemEntity.setTotalQuantity(itemDTO.getTotalQuantity());
                itemEntity.setItem(itemDefinition);
                itemEntity.setCurrencyRatio(itemDTO.getCurrencyRatio());
                if(itemDTO.getMods() != null) {
                    itemEntity.setMods(createItemModsListFromExistingModsOrAddNew(itemDTO));
                }

                itemEntityList.add(itemEntity);
            }

            System.out.printf("Saving a total of %s items to database%n", itemEntityList.size());

            itemEntityService.saveAll(itemEntityList);

            // save item icons
            if(!itemDefinitionEntities.isEmpty()) {
                itemDefinitionsService.saveAll(itemDefinitionEntities);
                itemDefinitionEntities.clear();
            }
        }
    }

    private List<ItemEntityMod> createItemModsListFromExistingModsOrAddNew(ItemDTO itemDTO) {
        List<ItemEntityMod> itemMods = new ArrayList<>();
        for(ItemEntityMod currentMod : itemDTO.getMods()) {
            if(existingItemEntityMods.containsKey(currentMod.getMod())) {
                itemMods.add(existingItemEntityMods.get(currentMod.getMod()));
            } else {
                Optional<ItemEntityMod> optionalMod = itemEntityModService.findFirstByMod(currentMod.getMod());
                if(optionalMod.isPresent()) {
                    ItemEntityMod mod = optionalMod.get();
                    existingItemEntityMods.put(mod.getMod(), mod);
                    itemMods.add(mod);
                } else {
                    ItemEntityMod newMod = new ItemEntityMod();
                    newMod.setMod(currentMod.getMod());

                    ItemEntityMod mod = itemEntityModService.save(newMod);
                    existingItemEntityMods.put(mod.getMod(), mod);
                    itemMods.add(mod);
                }
            }
        }
        
        return itemMods;
    }
}
