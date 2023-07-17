package com.exileeconomics.price;

import com.exileeconomics.entity.CurrencyRatioEntity;

import java.math.BigDecimal;

public final class SellableItemDTO {
    private BigDecimal price;
    private CurrencyRatioEntity currencyRatio;
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

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
