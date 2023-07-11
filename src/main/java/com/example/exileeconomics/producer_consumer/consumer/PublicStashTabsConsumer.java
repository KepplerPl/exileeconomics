package com.example.exileeconomics.producer_consumer.consumer;
import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;

import com.example.exileeconomics.entity.ItemEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.mapper.ItemDao;
import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.example.exileeconomics.repository.ItemEntityRepository;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class PublicStashTabsConsumer implements NoSuppressedRunnable{
    private final BlockingQueue<String> jsonResponsesQueue;
    private final HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions;
    private final ItemEntityRepository itemEntityRepository;
    private final CountDownLatch countDownLatchForCurrencyRatioInitialization;
    private final PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson;

    public PublicStashTabsConsumer(
            BlockingQueue<String> jsonResponsesQueue,
            HashMap<ItemDefinitionEnum, ItemDefinitionEntity> itemDefinitions,
            ItemEntityRepository itemEntityRepository,
            CountDownLatch countDownLatchForCurrencyRatioInitialization,
            PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson
    ) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.itemDefinitions = itemDefinitions;
        this.itemEntityRepository = itemEntityRepository;
        this.countDownLatchForCurrencyRatioInitialization = countDownLatchForCurrencyRatioInitialization;
        this.publicStashTabsDeserializerFromJson = publicStashTabsDeserializerFromJson;
    }

    @Override
    public void doRun() throws InterruptedException {
        countDownLatchForCurrencyRatioInitialization.await();

        if(jsonResponsesQueue.size() == 0) {
            System.out.println("Nothing to consume, returning...");
            return;
        }

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
                PublicStashTabsDao.class,
                publicStashTabsDeserializerFromJson
        );

        while(jsonResponsesQueue.iterator().hasNext()) {
            PublicStashTabsDao publicStashTabsDao = builder
                    .create()
                    .fromJson(jsonResponsesQueue.take(), PublicStashTabsDao.class);

            saveItems(publicStashTabsDao);
        }

        System.out.printf("Queue now has %d items%n", jsonResponsesQueue.size());
    }

    private void saveItems(PublicStashTabsDao publicStashTabsDao) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        if (!publicStashTabsDao.getItemDaos().isEmpty()) {
            for (ItemDao itemDao : publicStashTabsDao.getItemDaos()) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setPrice(itemDao.getPrice());
                itemEntity.setSoldQuantity(itemDao.getSoldQuantity());
                itemEntity.setTotalQuantity(itemDao.getTotalQuantity());
                itemEntity.setItem(itemDefinitions.get(itemDao.getItem()));
                itemEntity.setCurrencyRatio(itemDao.getCurrencyRatio());

                itemEntityList.add(itemEntity);
            }

            System.out.println("Saving a total of " + itemEntityList.size() + " items to database");

            itemEntityRepository.saveAll(itemEntityList);
        }
    }
}
