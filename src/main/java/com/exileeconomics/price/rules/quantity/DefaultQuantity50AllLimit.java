package com.exileeconomics.price.rules.quantity;

import java.util.Objects;

public  final class DefaultQuantity50AllLimit extends AbstractQuantityLimit {

    public DefaultQuantity50AllLimit() {
        quantityLowerLimit = 50;
        quantityUpperLimit = 99_999;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultQuantity50AllLimit that)) return false;
        return getQuantityLowerLimit() == that.getQuantityLowerLimit() && getQuantityUpperLimit() == that.getQuantityUpperLimit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantityLowerLimit(), getQuantityUpperLimit());
    }
}
