package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.price.exception.InvalidCurrencyException;
import com.example.exileeconomics.price.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SellableItemBuilder {
    private final BigDecimal maxPrice;

    private final CurrencyRatio currencyRatio;

    public SellableItemBuilder(CurrencyRatio currencyRatio) {
        this.currencyRatio = currencyRatio;
        this.maxPrice = new BigDecimal(300_000);
    }

    public SellableItem parsePrice(String price) throws InvalidCurrencyException, InvalidPriceException {
        String[] parts = price.split(" ");
        if(parts.length < 3) {
            throw new InvalidCurrencyException("Cannot parse currency " + price);
        }

        // check if the currency in which the sold item is listed is in one of the 3 acceptable currencies
        String currency = parts[2];
        if(!ParsableCurrencyEnum.contains(currency)) {
            throw new InvalidCurrencyException(String.format("Currency %s is not recognized as a valid currency", currency));
        }

        // the actual price is always the 2nd part of the string
        // examples
        // ~price 1/4000 divine
        // ~price 0.5 chaos
        // ~price 4 orb-of-conflict
        // ~b/o 1/82 divine
        // ~b/o 8 chaos
        // "~price  chaos" <-- not a mistake, that's how it arrives from the api(possible bug in the api itself)
        price = parts[1];
        if(price.trim().equals("")) {
            throw new InvalidPriceException(String.format("Price %s is not recognized as a valid price", price));
        }

        ParsableCurrencyEnum parsableCurrencyEnum = ParsableCurrencyEnum.fromString(parts[2]);
        ItemDefinitionEnum boughtFor = ParsableCurrencyEnum.parsableCurrencyEnumToItemDefinitionEnum(parsableCurrencyEnum);
        CurrencyRatioEntity currencyRatioInChaos = currencyRatio.getRatioFor(boughtFor);
        BigDecimal currencyRatio = new BigDecimal(currencyRatioInChaos.getChaos());

        BigDecimal resultingPrice;

        SellableItem sellableItem = new SellableItem();

        if(price.contains("/")) {
            String[] fractionParts = price.split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);

            BigDecimal numeratorBigDecimal = new BigDecimal(numerator).multiply(currencyRatio);
            BigDecimal denominatorBigDecimal = new BigDecimal(denominator);

            resultingPrice = numeratorBigDecimal.divide(denominatorBigDecimal, 4, RoundingMode.HALF_DOWN);
            sellableItem.setSoldQuantity(denominator);
        }else{
            resultingPrice = BigDecimal
                    .valueOf(Double.parseDouble(price))
                    .multiply(currencyRatio)
                    .setScale(4, RoundingMode.HALF_DOWN);
            sellableItem.setSoldQuantity(1);
        }

        if(resultingPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceException("Price is too large, got " + price);
        }

        sellableItem.setPrice(resultingPrice);
        sellableItem.setCurrencyRatio(currencyRatioInChaos);

        return sellableItem;
    }
}
