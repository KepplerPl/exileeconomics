package com.exileeconomics.price.rules.quantity;

public interface QuantityLimitInterface extends Comparable<QuantityLimitInterface>{
    int getQuantityLowerLimit();
    int getQuantityUpperLimit();
    @Override
    default int compareTo(QuantityLimitInterface o) {
        return Integer.compare(o.getQuantityUpperLimit(), getQuantityUpperLimit());
    }
}
