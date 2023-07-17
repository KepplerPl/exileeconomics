package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParsableCurrency {
    private static final Set<ItemDefinitionEnum> parsableCurrencies = new HashSet<>(List.of(
            ItemDefinitionEnum.CHAOS_ORB,
            ItemDefinitionEnum.DIVINE_ORB,
            ItemDefinitionEnum.AWAKENED_SEXTANT)
    );

    public static Set<ItemDefinitionEnum> getParsableCurrencies() {
        return parsableCurrencies;
    }
}
