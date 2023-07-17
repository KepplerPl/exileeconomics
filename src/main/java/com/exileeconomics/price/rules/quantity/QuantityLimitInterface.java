package com.exileeconomics.price.rules.quantity;

public interface QuantityLimitInterface extends Comparable<AbstractQuantityLimit>{

    int getQuantityLowerLimit();
    int getQuantityUpperLimit();

}
