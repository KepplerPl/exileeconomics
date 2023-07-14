package com.example.exileeconomics.price.rules.quantity;

import java.util.Objects;

public  class DefaultQuantity2550Limit extends AbstractQuantityLimit{
    public DefaultQuantity2550Limit() {
       quantityLowerLimit = 25;
       quantityUpperLimit = 50;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultQuantity2550Limit that)) return false;
        return getQuantityLowerLimit() == that.getQuantityLowerLimit() && getQuantityUpperLimit() == that.getQuantityUpperLimit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantityLowerLimit(), getQuantityUpperLimit());
    }

}
