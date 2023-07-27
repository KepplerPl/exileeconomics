package com.exileeconomics.mapper;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemEntityMod;

import java.math.BigDecimal;
import java.util.List;

public final class ItemDTO {
    private BigDecimal price;
    private CurrencyRatioEntity currencyRatio;
    private ItemDefinitionEnum item;
    private int totalQuantity;
    private int soldQuantity;
    private String icon;
    private List<ItemEntityMod> mods;

    public List<ItemEntityMod> getMods() {
        return mods;
    }

    public void setMods(List<ItemEntityMod> mods) {
        this.mods = mods;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CurrencyRatioEntity getCurrencyRatio() {
        return currencyRatio;
    }

    public void setCurrencyRatio(CurrencyRatioEntity currencyRatio) {
        this.currencyRatio = currencyRatio;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ItemDefinitionEnum getItem() {
        return item;
    }

    public void setItem(ItemDefinitionEnum item) {
        this.item = item;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
