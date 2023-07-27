package com.exileeconomics.mapper.deserializer;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.definitions.ItemDefinitionEnumCategoryMapper;
import com.exileeconomics.entity.ItemEntityMod;
import com.exileeconomics.mapper.ItemDTO;
import com.exileeconomics.price.SellableItemBuilder;
import com.exileeconomics.price.SellableItemDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PublicStashTabsDeserializer implements ApiDeserializer<List<ItemDTO>> {
    private final String activeLeague;
    private final SellableItemBuilder sellableItemBuilder;
    private final String[] parsableMods = {"enchantMods", "implicitMods", "explicitMods", "craftedMods", "fracturedMods", "veiledMods"};

    public PublicStashTabsDeserializer(String activeLeague, SellableItemBuilder sellableItemBuilder) {
        this.activeLeague = activeLeague;
        this.sellableItemBuilder = sellableItemBuilder;
    }

    @Override
    public List<ItemDTO> fromJson(JsonObject jsonObject) {
        List<JsonElement> stashes = jsonObject.get("stashes").getAsJsonArray().asList();

        List<ItemDTO> itemDTOList = new ArrayList<>();

        for (JsonElement stashObject : stashes) {
            if (stashObject.isJsonObject()) {
                JsonObject stash = stashObject.getAsJsonObject();
                if (!isStashValid(stash)) {
                    continue;
                }

                itemDTOList.addAll(itemFromJson(stash));
            }
        }

        return itemDTOList;
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

            ItemDefinitionEnum itemDefinitionEnum;
            try {
                itemDefinitionEnum = ItemDefinitionEnum.fromString(itemJson.get("baseType").getAsString());
            } catch (IllegalArgumentException e) {
                itemDefinitionEnum = ItemDefinitionEnum.fromString(itemJson.get("name").getAsString());
            }
            if(itemDefinitionEnum == null) {
                continue;
            }

            SellableItemDTO sellableItemDTO;
            try{
                sellableItemDTO = sellableItemBuilder.parsePrice(itemJson.get("note").getAsString());
            } catch (Exception e) {
                continue;
            }

            ItemDTO item = new ItemDTO();
            item.setPrice(sellableItemDTO.getPrice());
            item.setSoldQuantity(sellableItemDTO.getSoldQuantity());
            item.setTotalQuantity(
                    itemJson.has("stackSize") ?
                    itemJson.get("stackSize").getAsInt() : 1
            );
            item.setItem(itemDefinitionEnum);
            item.setIcon(itemJson.get("icon").getAsString());
            item.setCurrencyRatio(sellableItemDTO.getCurrencyRatio());

            if(itemJson.has("name") && !itemJson.get("name").getAsString().equals("") && ItemDefinitionEnum.contains(itemJson.get("name").getAsString().toLowerCase())) {
                List<ItemEntityMod> mods = new ArrayList<>();
                for(String parsableMod : parsableMods) {
                    if(itemJson.has(parsableMod)) {
                        for(JsonElement mod : itemJson.get(parsableMod).getAsJsonArray()){
                            ItemEntityMod itemEntityMod = new ItemEntityMod();
                            if(mod.getAsString().length() >= 255) {
                                itemEntityMod.setMod(mod.getAsString().substring(0, 254));
                            } else {
                                itemEntityMod.setMod(mod.getAsString());
                            }
                            mods.add(itemEntityMod);
                        }
                    }
                }
                item.setMods(mods);
            }

            items.add(item);
        }

        return items;
    }

    private boolean isItemValid(JsonObject itemJson) {
        return itemJson.has("note") &&
                !itemJson.get("note").isJsonNull() &&
                !itemJson.get("note").getAsString().trim().equals("~skip") &&
                (
                    ItemDefinitionEnum.contains(itemJson.get("baseType").getAsString()) ||
                    ItemDefinitionEnum.contains(itemJson.get("name").getAsString().toLowerCase())
                )
                ;
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
