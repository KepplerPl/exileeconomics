package com.example.exileeconomics.mapper.deserializer;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.mapper.ItemDao;
import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.price.exception.InvalidCurrencyException;
import com.example.exileeconomics.price.interfaces.PriceParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PublicStashTabsDeserializer implements ApiDeserializer<PublicStashTabsDao> {

    private final String activeLeague;
    private final PriceParser parser;

    public PublicStashTabsDeserializer(String activeLeague, com.example.exileeconomics.price.PriceParser parser) {
        this.activeLeague = activeLeague;
        this.parser = parser;
    }

    @Override
    public PublicStashTabsDao fromJson(JsonObject jsonObject) {
        PublicStashTabsDao publicStashTabsDao = new PublicStashTabsDao();
        List<JsonElement> stashes = jsonObject.get("stashes").getAsJsonArray().asList();

        List<ItemDao> items = null;

        for (JsonElement stashObject : stashes) {
            if (stashObject.isJsonObject()) {
                JsonObject stash = stashObject.getAsJsonObject();
                if (!isStashValid(stash)) {
                    continue;
                }

                items = itemFromJson(stash);
            }
        }

        if (items == null) {
            return publicStashTabsDao;
        }
        publicStashTabsDao.setItemDaos(items);

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
            BigDecimal price;
            try{
                price = parser.parsePrice(itemJson.get("note").getAsString());
            } catch (InvalidCurrencyException e) {
                continue;
            }

            ItemDao item = new ItemDao();

            item.setPrice(price);
            item.setStackSize(itemJson.get("stackSize").getAsInt());
            item.setBaseType(itemJson.get("baseType").getAsString());

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
