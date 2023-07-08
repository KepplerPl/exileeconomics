package com.example.exileeconomics.price.interfaces;

import com.example.exileeconomics.price.exception.InvalidCurrencyException;

import java.math.BigDecimal;

public interface PriceParser {
    BigDecimal parsePrice(String price) throws InvalidCurrencyException;

}
