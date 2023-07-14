package com.example.exileeconomics.price.rules;

import com.example.exileeconomics.price.rules.quantity.QuantityLimitInterface;

import java.util.Set;

public interface ItemPriceRuleInterface {

    int getLimit();
    int getOffset();
    Set<? extends QuantityLimitInterface> getQuantityLimits();

}
