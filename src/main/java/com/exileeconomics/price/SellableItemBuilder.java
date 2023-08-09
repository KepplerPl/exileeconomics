package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.price.exception.InvalidCurrencyException;
import com.exileeconomics.price.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class SellableItemBuilder {
    private final HashMap<String, ItemDefinitionEnum> currencyApiNameToEnum = new HashMap<>(){{
       put("chaos", ItemDefinitionEnum.CHAOS_ORB);
       put("divine", ItemDefinitionEnum.DIVINE_ORB);
    }};

    private final BigDecimal maxPrice;
    private final ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap;

    public SellableItemBuilder(ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap) {
        this.maxPrice = new BigDecimal(300_000);
        this.currencyRatioMap = currencyRatioMap;
    }

    public SellableItemDTO parsePrice(String price) throws InvalidCurrencyException, InvalidPriceException {
        String[] parts = price.split(" ");
        if(parts.length < 3) {
            throw new InvalidCurrencyException(String.format("Cannot parse currency from price %s", price));
        }

        // the actual price is always the 2nd part of the string
        // examples
        // ~price 1/4000 divine
        // ~price 0.5 chaos
        // ~price 4 orb-of-conflict
        // ~b/o 1/82 divine
        // ~b/o 8 chaos
        // "~price  chaos" - not a mistake, that's how it arrives from the api
        price = parts[1];
        if(price.trim().isEmpty()) {
            throw new InvalidPriceException("Price is an empty string");
        }

        if(!currencyApiNameToEnum.containsKey(parts[2])) {
            throw new InvalidCurrencyException(String.format("Currency %s is not recognized as a valid currency", parts[2]));
        }

        ItemDefinitionEnum boughtFor = currencyApiNameToEnum.get(parts[2]);
        CurrencyRatioEntity currencyRatioInChaos = currencyRatioMap.get(boughtFor);
        BigDecimal currencyRatio = currencyRatioInChaos.getChaos();

        BigDecimal resultingPrice;

        SellableItemDTO sellableItemDTO = new SellableItemDTO();

        if(price.contains("/")) {
            String[] fractionParts = price.split("/");
            int denominator = Integer.parseInt(fractionParts[1]);

            resultingPrice = calculatePriceWhenFraction(price, currencyRatio);
            sellableItemDTO.setSoldQuantity(denominator);
        }else{
            resultingPrice = calculatePriceWhenDouble(price, currencyRatio);
            sellableItemDTO.setSoldQuantity(1);
        }

        if(resultingPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceException(String.format("Price is too large, got %s", price));
        }

        sellableItemDTO.setPrice(resultingPrice);
        sellableItemDTO.setCurrencyRatio(currencyRatioInChaos);

        return sellableItemDTO;
    }

    private BigDecimal calculatePriceWhenDouble(String price, BigDecimal currencyRatio) {
        return BigDecimal
                .valueOf(Double.parseDouble(price))
                .multiply(currencyRatio)
                .setScale(4, RoundingMode.HALF_DOWN);
    }

    private BigDecimal calculatePriceWhenFraction(String price, BigDecimal currencyRatio) {
        String[] fractionParts = price.split("/");
        int numerator = Integer.parseInt(fractionParts[0]);
        int denominator = Integer.parseInt(fractionParts[1]);

        BigDecimal numeratorBigDecimal = new BigDecimal(numerator).multiply(currencyRatio);
        BigDecimal denominatorBigDecimal = new BigDecimal(denominator);

        return numeratorBigDecimal.divide(denominatorBigDecimal, 4, RoundingMode.HALF_DOWN);
    }
}
