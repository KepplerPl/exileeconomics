package com.exileeconomics.price.rules.quantity;

import java.util.Objects;

public  final class DefaultQuantity025Limit extends AbstractQuantityLimit {

    public DefaultQuantity025Limit() {
        super(0, 25);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultQuantity025Limit that)) return false;
        return getQuantityLowerLimit() == that.getQuantityLowerLimit() && getQuantityUpperLimit() == that.getQuantityUpperLimit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantityLowerLimit(), getQuantityUpperLimit());
    }
}
