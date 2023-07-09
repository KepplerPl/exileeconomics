package com.example.exileeconomics.producer_consumer.consumer;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;

import com.example.exileeconomics.entity.ItemEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.mapper.ItemDao;
import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.example.exileeconomics.repository.ItemEntityRepository;
import com.google.gson.GsonBuilder;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class PublicStashTabsConsumer implements NoSuppressedRunnable{
    private final BlockingQueue<String> jsonResponsesQueue;
    private final HashMap<String, ItemDefinitionEntity> itemDefinitions;
    private final ItemEntityRepository itemEntityRepository;
    private final CountDownLatch countDownLatch;
    private final PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson;

    public PublicStashTabsConsumer(
            BlockingQueue<String> jsonResponsesQueue,
            HashMap<String, ItemDefinitionEntity> itemDefinitions,
            ItemEntityRepository itemEntityRepository,
            CountDownLatch countDownLatch,
            PublicStashTabsDeserializerFromJson publicStashTabsDeserializerFromJson
    ) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.itemDefinitions = itemDefinitions;
        this.itemEntityRepository = itemEntityRepository;
        this.countDownLatch = countDownLatch;
        this.publicStashTabsDeserializerFromJson = publicStashTabsDeserializerFromJson;
    }

    @Override
    public void doRun() throws InterruptedException {
        countDownLatch.await();

        System.out.println("Starting consumer");

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(
                PublicStashTabsDao.class,
                publicStashTabsDeserializerFromJson
        );

        for(int i = 0; i < jsonResponsesQueue.size(); i++) {
            PublicStashTabsDao publicStashTabsDao = builder
                    .create()
                    .fromJson(jsonResponsesQueue.take(), PublicStashTabsDao.class);

            saveItems(publicStashTabsDao);
        }
    }

    private void saveItems(PublicStashTabsDao publicStashTabsDao) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        if (!publicStashTabsDao.getItemDaos().isEmpty()) {
            for (ItemDao itemDao : publicStashTabsDao.getItemDaos()) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setPrice(itemDao.getPrice());
                itemEntity.setQuantity(itemDao.getStackSize());
                itemEntity.setItemDefinition(itemDefinitions.get(itemDao.getBaseType().toLowerCase()));
                itemEntity.setPrice(itemDao.getPrice());

                itemEntityList.add(itemEntity);
            }

            System.out.println("Saving a total of " + itemEntityList.size() + " items to database");

            itemEntityRepository.saveAll(itemEntityList);
        }
    }
}
