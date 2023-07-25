package com.exileeconomics.price.dto;

import com.exileeconomics.definitions.ItemDefinitionEnum;

public class ItemDefinitionResponseDTO {
    private ItemDefinitionEnum itemDefinitionEnum;
    private String name;
    private String icon;

    public ItemDefinitionEnum getItemDefinitionEnum() {
        return itemDefinitionEnum;
    }

    public void setItemDefinitionEnum(ItemDefinitionEnum itemDefinitionEnum) {
        this.itemDefinitionEnum = itemDefinitionEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
