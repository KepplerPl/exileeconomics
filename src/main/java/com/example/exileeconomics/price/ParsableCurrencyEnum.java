package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ParsableCurrencyEnum {
    CHAOS("chaos"),
    DIVINE("divine"),
    SEXTANT("awakened-sextant");
    private final String name;

    ParsableCurrencyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, ParsableCurrencyEnum> stringMap;

    public static ParsableCurrencyEnum fromString(String value) {
        if(!ParsableCurrencyEnum.contains(value)) {
            throw new IllegalArgumentException("Unrecognized enum value, got  " + value);
        }

        return stringMap.get(value);
    }

    private static final Set<String> enumAsString = new HashSet<>();

    public static boolean contains(String name) {
        return enumAsString.contains(name.toLowerCase());
    }

    private static final Map<ParsableCurrencyEnum, ItemDefinitionEnum> parsableCurrencyEnumItemDefinitionEnumMap;

    public static ItemDefinitionEnum parsableCurrencyEnumToItemDefinitionEnum(ParsableCurrencyEnum parsableCurrency) {
        return parsableCurrencyEnumItemDefinitionEnumMap.get(parsableCurrency);
    }

    static {
        parsableCurrencyEnumItemDefinitionEnumMap = new HashMap<>()
        {{
            put(CHAOS, ItemDefinitionEnum.CHAOS_ORB);
            put(DIVINE, ItemDefinitionEnum.DIVINE_ORB);
            put(SEXTANT, ItemDefinitionEnum.AWAKENED_SEXTANT);
        }};

        stringMap = Arrays.stream(values())
                .collect(Collectors.toMap(ParsableCurrencyEnum::getName, Function.identity()));

        for (ParsableCurrencyEnum c : ParsableCurrencyEnum.values()) {
            enumAsString.add(c.getName());
        }
    }
}
