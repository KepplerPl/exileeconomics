package com.exileeconomics.producer_consumer.consumer;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.producer_consumer.NoSuppressedRunnable;

import com.exileeconomics.entity.ItemEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.mapper.ItemDTO;
import com.exileeconomics.mapper.PublicStashTabsDTO;
import com.exileeconomics.mapper.serializer.PublicStashTabsJsonDeserializer;
import com.exileeconomics.service.ItemEntityService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class PublicStashTabsConsumer implements NoSuppressedRunnable {
    private final BlockingQueue<String> jsonResponsesQueue;
    private final HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions;
    private final ItemEntityService itemEntityService;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;
    private final PublicStashTabsJsonDeserializer publicStashTabsJsonDeserializer;

    public PublicStashTabsConsumer(
            BlockingQueue<String> jsonResponsesQueue,
            HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions,
            ItemEntityService itemEntityService,
            CountDownLatch countDownLatchForCurrencyRatioInitialization,
            PublicStashTabsJsonDeserializer publicStashTabsJsonDeserializer
    ) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.itemDefinitions = itemDefinitions;
        this.itemEntityService = itemEntityService;
        this.countDownLatchForCurrencyRatioInitialization = countDownLatchForCurrencyRatioInitialization;
        this.publicStashTabsJsonDeserializer = publicStashTabsJsonDeserializer;
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
                PublicStashTabsDTO.class,
                publicStashTabsJsonDeserializer
        );
        Gson gson = builder.create();

        while(jsonResponsesQueue.iterator().hasNext()) {
            PublicStashTabsDTO publicStashTabsDTO = gson.fromJson(jsonResponsesQueue.take(), PublicStashTabsDTO.class);

            saveItems(publicStashTabsDTO);
        }

        System.out.printf("Queue now has %d items%n", jsonResponsesQueue.size());
    }

    private void saveItems(PublicStashTabsDTO publicStashTabsDTO) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        if (!publicStashTabsDTO.getItemDTOS().isEmpty()) {
            for (ItemDTO itemDTO : publicStashTabsDTO.getItemDTOS()) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setPrice(itemDTO.getPrice());
                itemEntity.setSoldQuantity(itemDTO.getSoldQuantity());
                itemEntity.setTotalQuantity(itemDTO.getTotalQuantity());
                itemEntity.setItem(itemDefinitions.get(itemDTO.getItem()));
                itemEntity.setCurrencyRatio(itemDTO.getCurrencyRatio());

                itemEntityList.add(itemEntity);
            }

            System.out.printf("Saving a total of %s items to database%n", itemEntityList.size());

            itemEntityService.saveAll(itemEntityList);
        }
    }
}
