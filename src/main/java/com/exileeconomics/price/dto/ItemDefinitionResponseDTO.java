package com.exileeconomics.price.dto;

import com.exileeconomics.definitions.ItemDefinitionEnum;

public class ItemDefinitionResponseDTO {
    private ItemDefinitionEnum machineName;
    private String readableName;
    private String icon;

    public ItemDefinitionEnum getMachineName() {
        return machineName;
    }

    public void setMachineName(ItemDefinitionEnum machineName) {
        this.machineName = machineName;
    }

    public String getReadableName() {
        return readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
