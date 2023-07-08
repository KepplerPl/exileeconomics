package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.price.exception.InvalidCurrencyException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceParser {
    private final Set<String> currencies = new HashSet<>(List.of(
            ItemDefinitionEnum.CHAOS_ORB.getName(),
            ItemDefinitionEnum.DIVINE_ORB.getName())
    );

    private final Pattern pattern;

    public PriceParser() {
        this.pattern = buildRegex();
    }

    public BigDecimal parsePrice(String price) throws InvalidCurrencyException {
        Matcher matcher = pattern.matcher(price);
        if (!matcher.matches()) {
            throw new InvalidCurrencyException("Currency is not supported");
        }

        String[] parts = price.split(" ");
        if(parts.length == 0) {
            throw new InvalidCurrencyException("Cannot parse currency");
        }

        // the actual price is always the 2nd part of the string
        // examples
        // ~price 1/4000 divine
        // ~price 0.5 chaos
        // ~price 4 orb-of-conflict
        price = parts[1];

        if(price.contains("/")) {
            String[] fractionParts = price.split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);

            return new BigDecimal(numerator/denominator).setScale(4, RoundingMode.UNNECESSARY);
        }

        return new BigDecimal(Integer.parseInt(price)).setScale(4, RoundingMode.UNNECESSARY);
    }

    private Pattern buildRegex() {
        StringBuilder regexRaw = new StringBuilder("(");

        for(String currency : currencies) {
            regexRaw.append(currency).append("|");
        }

        regexRaw.replace(regexRaw.length() - 1, regexRaw.length(), ")");

        return Pattern.compile(regexRaw.toString());
    }
}
