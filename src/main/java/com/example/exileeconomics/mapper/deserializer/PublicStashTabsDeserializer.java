package com.example.exileeconomics.mapper.deserializer;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.mapper.ItemDao;
import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.price.SellableItemBuilder;
import com.example.exileeconomics.price.SellableItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PublicStashTabsDeserializer implements ApiDeserializer<PublicStashTabsDao> {
    private final String activeLeague;
    private final SellableItemBuilder sellableItemBuilder;

    public PublicStashTabsDeserializer(String activeLeague, SellableItemBuilder sellableItemBuilder) {
        this.activeLeague = activeLeague;
        this.sellableItemBuilder = sellableItemBuilder;
    }

    @Override
    public PublicStashTabsDao fromJson(JsonObject jsonObject) {
        PublicStashTabsDao publicStashTabsDao = new PublicStashTabsDao();
        List<JsonElement> stashes = jsonObject.get("stashes").getAsJsonArray().asList();

        List<List<ItemDao>> items = new ArrayList<>();
        List<ItemDao> itemDaoList = new ArrayList<>();

        for (JsonElement stashObject : stashes) {
            if (stashObject.isJsonObject()) {
                JsonObject stash = stashObject.getAsJsonObject();
                if (!isStashValid(stash)) {
                    continue;
                }

                items.add(itemFromJson(stash));
            }
        }

        if(!items.isEmpty()) {
            for(List<ItemDao> itemDaoLoopList : items) {
                if(!itemDaoLoopList.isEmpty()) {
                    itemDaoList.addAll(itemDaoLoopList);
                }
            }
        }
        publicStashTabsDao.setItemDaos(itemDaoList);

        return publicStashTabsDao;
    }

    private List<ItemDao> itemFromJson(JsonObject stash) {
        List<ItemDao> items = new ArrayList<>();

        if (!stash.has("items") || stash.get("items").isJsonNull()) {
            return items;
        }

        List<JsonElement> itemList = stash.get("items").getAsJsonArray().asList();

        if(itemList.isEmpty()) {
            return items;
        }

        for (JsonElement jsonElementItem : itemList) {
            if (!jsonElementItem.isJsonObject()) {
                continue;
            }

            JsonObject itemJson = jsonElementItem.getAsJsonObject();
            if (!isItemValid(itemJson)) {
                continue;
            }

            SellableItem sellableItem;
            try{
                sellableItem = sellableItemBuilder.parsePrice(itemJson.get("note").getAsString());
            } catch (Exception e) {
                continue;
            }
            ItemDefinitionEnum itemDefinitionEnum = ItemDefinitionEnum.fromString(itemJson.get("baseType").getAsString());

            ItemDao item = new ItemDao();
            item.setPrice(sellableItem.getPrice());
            item.setSoldQuantity(sellableItem.getSoldQuantity());
            item.setTotalQuantity(itemJson.get("stackSize").getAsInt());
            item.setItem(itemDefinitionEnum);
            item.setCurrencyRatio(sellableItem.getCurrencyRatio());

            items.add(item);
        }

        return items;
    }

    private boolean isItemValid(JsonObject itemJson) {
        return itemJson.has("note") &&
                !itemJson.get("note").isJsonNull() &&
                !itemJson.get("note").getAsString().trim().equals("~skip") &&
                itemJson.has("stackSize") &&
                !itemJson.get("stackSize").isJsonNull() &&
                ItemDefinitionEnum.contains(itemJson.get("baseType").getAsString());
    }

    private boolean isStashValid(JsonObject stash) {
        return stash.has("items") &&
                !stash.get("items").isJsonNull() &&
                !stash.get("items").getAsJsonArray().isEmpty() &&
                stash.has("league") &&
                !stash.get("league").isJsonNull() &&
                stash.get("league").getAsString().trim().toLowerCase().equals(activeLeague);
    }
}
