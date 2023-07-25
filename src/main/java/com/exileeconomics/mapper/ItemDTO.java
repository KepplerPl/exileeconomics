package com.exileeconomics.mapper;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;

import java.math.BigDecimal;

public final class ItemDTO {
    private BigDecimal price;
    private CurrencyRatioEntity currencyRatio;
    private ItemDefinitionEnum item;
    private int totalQuantity;
    private int soldQuantity;

    private String icon;

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
