package com.exileeconomics.price.rules.quantity;

public class AbstractQuantityLimit implements QuantityLimitInterface{
    protected int quantityLowerLimit;
    protected int quantityUpperLimit;

    public AbstractQuantityLimit(int quantityLowerLimit, int quantityUpperLimit) {
        this.quantityLowerLimit = quantityLowerLimit;
        this.quantityUpperLimit = quantityUpperLimit;
    }

    @Override
    public int getQuantityLowerLimit() {
        return quantityLowerLimit;
    }

    public void setQuantityLowerLimit(int quantityLowerLimit) {
        this.quantityLowerLimit = quantityLowerLimit;
    }

    @Override
    public int getQuantityUpperLimit() {
        return quantityUpperLimit;
    }

    public void setQuantityUpperLimit(int quantityUpperLimit) {
        this.quantityUpperLimit = quantityUpperLimit;
    }
}
