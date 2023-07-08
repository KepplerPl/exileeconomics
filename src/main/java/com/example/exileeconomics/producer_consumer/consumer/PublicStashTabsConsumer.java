package com.example.exileeconomics.producer_consumer.consumer;
import com.example.exileeconomics.producer_consumer.NoSuppressedRunnable;

import com.example.exileeconomics.Properties;
import com.example.exileeconomics.entity.Item;
import com.example.exileeconomics.entity.ItemDefinition;
import com.example.exileeconomics.mapper.ItemDao;
import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.mapper.deserializer.PublicStashTabsDeserializer;
import com.example.exileeconomics.mapper.serializer.PublicStashTabsDeserializerFromJson;
import com.example.exileeconomics.repository.ItemEntityRepository;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class PublicStashTabsConsumer implements NoSuppressedRunnable{
    private final BlockingQueue<String> jsonResponsesQueue;
    private final Properties properties;
    private final HashMap<String, ItemDefinition> itemDefinitions;
    private final ItemEntityRepository itemEntityRepository;

    public PublicStashTabsConsumer(BlockingQueue<String> jsonResponsesQueue, Properties properties, HashMap<String, ItemDefinition> itemDefinitions, ItemEntityRepository itemEntityRepository) {
        this.jsonResponsesQueue = jsonResponsesQueue;
        this.properties = properties;
        this.itemDefinitions = itemDefinitions;
        this.itemEntityRepository = itemEntityRepository;
    }

    @Override
    public void doRun() throws InterruptedException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PublicStashTabsDao.class, new PublicStashTabsDeserializerFromJson(new PublicStashTabsDeserializer(properties.getActiveLeague())));

        for(int i = 0; i < jsonResponsesQueue.size(); i++) {
            PublicStashTabsDao publicStashTabsDao = builder
                    .create()
                    .fromJson(jsonResponsesQueue.take(), PublicStashTabsDao.class);

            saveItems(publicStashTabsDao);
        }
    }

    private void saveItems(PublicStashTabsDao publicStashTabsDao) {
        List<Item> itemList = new ArrayList<>();
        if (!publicStashTabsDao.getItemDaos().isEmpty()) {
            for (ItemDao itemDao : publicStashTabsDao.getItemDaos()) {
                Item item = new Item();
                item.setNote(itemDao.getNote());
                item.setQuantity(itemDao.getStackSize());
                item.setItemDefinition(itemDefinitions.get(itemDao.getBaseType().toLowerCase()));
                BigDecimal bigDecimal = new BigDecimal(1);
                item.setPrice(bigDecimal);

                itemList.add(item);
            }

            System.out.println("Saving a total of " + itemList.size() + " items to database");

            itemEntityRepository.saveAll(itemList);
        }

        System.out.println("No items to save to database");
    }
}
