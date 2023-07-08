package com.example.exileeconomics.price;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class PriceCalculator implements com.example.exileeconomics.price.interfaces.PriceCalculator {
    @Override
    public BigDecimal calculatePrice(BigDecimal dividend, BigDecimal divisor, int scale) {
        return dividend.divide(dividend, scale, RoundingMode.HALF_UP);
    }
}
