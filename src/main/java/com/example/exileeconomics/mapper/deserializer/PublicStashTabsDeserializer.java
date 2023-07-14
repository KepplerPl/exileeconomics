package com.example.exileeconomics.mapper.deserializer;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.mapper.ItemDTO;
import com.example.exileeconomics.mapper.PublicStashTabsDTO;
import com.example.exileeconomics.price.SellableItemBuilder;
import com.example.exileeconomics.price.SellableItemDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PublicStashTabsDeserializer implements ApiDeserializer<PublicStashTabsDTO> {
    private final String activeLeague;
    private final SellableItemBuilder sellableItemBuilder;

    public PublicStashTabsDeserializer(String activeLeague, SellableItemBuilder sellableItemBuilder) {
        this.activeLeague = activeLeague;
        this.sellableItemBuilder = sellableItemBuilder;
    }

    @Override
    public PublicStashTabsDTO fromJson(JsonObject jsonObject) {
        PublicStashTabsDTO publicStashTabsDTO = new PublicStashTabsDTO();
        List<JsonElement> stashes = jsonObject.get("stashes").getAsJsonArray().asList();

        List<List<ItemDTO>> items = new ArrayList<>();
        List<ItemDTO> itemDTOList = new ArrayList<>();

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
            for(List<ItemDTO> itemDTOLoopList : items) {
                if(!itemDTOLoopList.isEmpty()) {
                    itemDTOList.addAll(itemDTOLoopList);
                }
            }
        }
        publicStashTabsDTO.setItemDTOS(itemDTOList);

        return publicStashTabsDTO;
    }

    private List<ItemDTO> itemFromJson(JsonObject stash) {
        List<ItemDTO> items = new ArrayList<>();

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

            SellableItemDTO sellableItemDTO;
            try{
                sellableItemDTO = sellableItemBuilder.parsePrice(itemJson.get("note").getAsString());
            } catch (Exception e) {
                continue;
            }

            ItemDefinitionEnum itemDefinitionEnum = ItemDefinitionEnum.fromString(itemJson.get("baseType").getAsString());

            ItemDTO item = new ItemDTO();
            item.setPrice(sellableItemDTO.getPrice());
            item.setSoldQuantity(sellableItemDTO.getSoldQuantity());
            item.setTotalQuantity(itemJson.get("stackSize").getAsInt());
            item.setItem(itemDefinitionEnum);
            item.setCurrencyRatio(sellableItemDTO.getCurrencyRatio());

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
