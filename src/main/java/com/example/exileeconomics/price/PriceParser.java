package com.example.exileeconomics.price;

import com.example.exileeconomics.price.exception.InvalidCurrencyException;
import com.example.exileeconomics.price.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceParser {
    private final BigDecimal maxPrice;

    private final CurrencyRatio currencyRatio;

    public PriceParser(CurrencyRatio currencyRatio) {
        this.currencyRatio = currencyRatio;
        this.maxPrice = new BigDecimal(500000);
    }

    public BigDecimal parsePrice(String price) throws InvalidCurrencyException, InvalidPriceException {
        String[] parts = price.split(" ");
        if(parts.length < 3) {
            throw new InvalidCurrencyException("Cannot parse currency " + price);
        }
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
        if(price.equals("")) {
            throw new InvalidPriceException(String.format("Price %s is not recognized as a valid price", price));
        }

        ParsableCurrencyEnum parsableCurrencyEnum = ParsableCurrencyEnum.fromString(parts[2]);
        Integer currencyRatioInChaos = currencyRatio.getRatioFor(ParsableCurrencyEnum.parsableCurrencyEnumToItemDefinitionEnum(parsableCurrencyEnum));

        BigDecimal resultingPrice;

        if(price.contains("/")) {
            String[] fractionParts = price.split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);

            BigDecimal currencyRatio = new BigDecimal(currencyRatioInChaos);
            BigDecimal numeratorBigDecimal = new BigDecimal(numerator).multiply(currencyRatio);
            BigDecimal denominatorBigDecimal = new BigDecimal(denominator);

            resultingPrice = numeratorBigDecimal.divide(denominatorBigDecimal, 4, RoundingMode.HALF_DOWN);
        }else{
            resultingPrice = BigDecimal.valueOf(Double.parseDouble(price)).setScale(4, RoundingMode.HALF_DOWN);
        }

        resultingPrice = resultingPrice.multiply(new BigDecimal(currencyRatioInChaos));

        if(resultingPrice.compareTo(maxPrice) > 0) {
            throw new InvalidPriceException("Price is too large, got " + price);
        }

        return resultingPrice.multiply(new BigDecimal(currencyRatioInChaos));
    }
}
