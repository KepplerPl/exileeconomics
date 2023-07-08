package com.example.exileeconomics.price;

import java.math.BigDecimal;

public class PriceParser implements com.example.exileeconomics.price.interfaces.PriceParser {

    public long parsePrice(String price) {
        if(!price.contains("dollars") || !price.contains("euros")) {
            throw new InvalidArgumentException();
        }
        // do stuff here
    }

}
