package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;

import java.math.BigDecimal;

public class SellableItem {
    private BigDecimal price;
    private CurrencyRatioEntity currencyRatio;
    private int totalQuantity;
    private int soldQuantity;

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
