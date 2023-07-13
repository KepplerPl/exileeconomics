package com.example.exileeconomics.price;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.price.exception.InvalidCurrencyException;
import com.example.exileeconomics.price.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class SellableItemBuilder {
    private final HashMap<String, ItemDefinitionEnum> currencyApiNameToEnum = new HashMap<>(){{
       put("chaos", ItemDefinitionEnum.CHAOS_ORB);
       put("divine", ItemDefinitionEnum.DIVINE_ORB);
       put("awakened-sextant", ItemDefinitionEnum.AWAKENED_SEXTANT);
    }};

    private final BigDecimal maxPrice;
    private final CurrencyRatioProducer currencyRatioProducer;

    public SellableItemBuilder(CurrencyRatioProducer currencyRatioProducer) {
        this.currencyRatioProducer = currencyRatioProducer;
        this.maxPrice = new BigDecimal(300_000);
    }

    public SellableItemDTO parsePrice(String price) throws InvalidCurrencyException, InvalidPriceException {
        String[] parts = price.split(" ");
        if(parts.length < 3) {
            throw new InvalidCurrencyException(String.format("Cannot parse currency %s", price));
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

        if(!currencyApiNameToEnum.containsKey(parts[2])) {
            throw new InvalidCurrencyException(String.format("Currency %s is not recognized as a valid currency", parts[2]));
        }

        ItemDefinitionEnum boughtFor = currencyApiNameToEnum.get(parts[2]);
        CurrencyRatioEntity currencyRatioInChaos = currencyRatioProducer.getRatioFor(boughtFor);
        BigDecimal currencyRatio = currencyRatioInChaos.getChaos();

        BigDecimal resultingPrice;

        SellableItemDTO sellableItemDTO = new SellableItemDTO();

        if(price.contains("/")) {
            String[] fractionParts = price.split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);

            BigDecimal numeratorBigDecimal = new BigDecimal(numerator).multiply(currencyRatio);
            BigDecimal denominatorBigDecimal = new BigDecimal(denominator);

            resultingPrice = numeratorBigDecimal.divide(denominatorBigDecimal, 4, RoundingMode.HALF_DOWN);
            sellableItemDTO.setSoldQuantity(denominator);
        }else{
            resultingPrice = BigDecimal
                    .valueOf(Double.parseDouble(price))
                    .multiply(currencyRatio)
                    .setScale(4, RoundingMode.HALF_DOWN);
            sellableItemDTO.setSoldQuantity(1);
        }

        if(resultingPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceException(String.format("Price is too large, got %s", price));
        }

        sellableItemDTO.setPrice(resultingPrice);
        sellableItemDTO.setCurrencyRatio(currencyRatioInChaos);

        return sellableItemDTO;
    }
}
