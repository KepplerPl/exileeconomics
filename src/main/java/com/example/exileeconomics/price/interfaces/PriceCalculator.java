package com.example.exileeconomics.price.interfaces;

import java.math.BigDecimal;

public interface PriceCalculator {
    BigDecimal calculatePrice(BigDecimal dividend, BigDecimal divisor, int scale);
}
