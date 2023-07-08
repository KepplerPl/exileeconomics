package com.example.exileeconomics.mapper;

import java.math.BigDecimal;

public class ItemDao {
    private BigDecimal price; //price
    private String baseType; //name
    private Integer stackSize; //quantity

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public Integer getStackSize() {
        return stackSize;
    }

    public void setStackSize(Integer stackSize) {
        this.stackSize = stackSize;
    }
}
