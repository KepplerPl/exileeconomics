package com.exileeconomics.price;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.exception.InvalidCurrencyException;
import com.exileeconomics.price.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SellableItemBuilderTest {

    private final SellableItemBuilder sellableItemBuilder;

    public SellableItemBuilderTest() {
        ConcurrentMap<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioEntityConcurrentMap = new ConcurrentHashMap<>();
        CurrencyRatioEntity currencyRatio = new CurrencyRatioEntity();
        currencyRatio.setChaos(new BigDecimal(100));

        ItemDefinitionEntity itemDefinitionEntity = new ItemDefinitionEntity();
        itemDefinitionEntity.setName(ItemDefinitionEnum.DIVINE_ORB.getName());
        itemDefinitionEntity.setId(1L);
        currencyRatio.setItemDefinitionEntity(itemDefinitionEntity);
        currencyRatio.setId(1L);
        currencyRatioEntityConcurrentMap.put(ItemDefinitionEnum.DIVINE_ORB, currencyRatio);

        sellableItemBuilder = new SellableItemBuilder(currencyRatioEntityConcurrentMap);
    }
    @Test
    public void testSellableItemDTO_noSlashInPrice() throws InvalidPriceException, InvalidCurrencyException {
        CurrencyRatioEntity currencyRatio = new CurrencyRatioEntity();
        currencyRatio.setChaos(new BigDecimal(100));

        ItemDefinitionEntity itemDefinitionEntity = new ItemDefinitionEntity();
        itemDefinitionEntity.setName(ItemDefinitionEnum.DIVINE_ORB.getName());
        itemDefinitionEntity.setId(1L);
        currencyRatio.setItemDefinitionEntity(itemDefinitionEntity);
        currencyRatio.setId(1L);

        SellableItemDTO expectedResult = new SellableItemDTO();
        expectedResult.setPrice(new BigDecimal(10000).setScale(4, RoundingMode.UNNECESSARY));
        expectedResult.setSoldQuantity(1);
        expectedResult.setCurrencyRatio(currencyRatio);

        SellableItemDTO actualResult = sellableItemBuilder.parsePrice("~price 100 divine");

        assertEquals(expectedResult.getPrice(), actualResult.getPrice());
        assertEquals(expectedResult.getSoldQuantity(), actualResult.getSoldQuantity());
        assertEquals(expectedResult.getCurrencyRatio().getChaos(), actualResult.getCurrencyRatio().getChaos());
    }
    @Test
    public void testSellableItemDTO_withSlashInPrice() throws InvalidPriceException, InvalidCurrencyException {
        CurrencyRatioEntity currencyRatio = new CurrencyRatioEntity();
        currencyRatio.setChaos(new BigDecimal(100));

        ItemDefinitionEntity itemDefinitionEntity = new ItemDefinitionEntity();
        itemDefinitionEntity.setName(ItemDefinitionEnum.DIVINE_ORB.getName());
        itemDefinitionEntity.setId(1L);
        currencyRatio.setItemDefinitionEntity(itemDefinitionEntity);
        currencyRatio.setId(1L);

        SellableItemDTO expectedResult = new SellableItemDTO();
        expectedResult.setPrice(new BigDecimal(500).setScale(4, RoundingMode.UNNECESSARY));
        expectedResult.setSoldQuantity(20);
        expectedResult.setCurrencyRatio(currencyRatio);

        SellableItemDTO actualResult = sellableItemBuilder.parsePrice("~price 100/20 divine");

        assertEquals(expectedResult.getPrice(), actualResult.getPrice());
        assertEquals(expectedResult.getSoldQuantity(), actualResult.getSoldQuantity());
        assertEquals(expectedResult.getCurrencyRatio().getChaos(), actualResult.getCurrencyRatio().getChaos());
    }

    @Test
    public void testThrowsInvalidCurrencyException_onPriceLength() {
        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class, () -> sellableItemBuilder.parsePrice("wrong size"));
        assertTrue(exception.getMessage().contains("Cannot parse currency from price wrong size"));
    }

    @Test
    public void testThrowsInvalidPriceException_onPriceEmpty() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> sellableItemBuilder.parsePrice("~price  divine"));
        assertTrue(exception.getMessage().contains("Price is an empty string"));
    }

    @Test
    public void testThrowsInvalidCurrencyException_onCurrencyRecognized() {
        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class, () -> sellableItemBuilder.parsePrice("~price 3 exalted"));
        assertTrue(exception.getMessage().contains("Currency exalted is not recognized as a valid currency"));
    }

    @Test
    public void testThrowsInvalidCurrencyException_onCurrencyNotExistingInMap() {
        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class, () -> sellableItemBuilder.parsePrice("~price 3 exalted"));
        assertTrue(exception.getMessage().contains("Currency exalted is not recognized as a valid currency"));
    }

    @Test
    public void testThrowsInvalidPriceException_onPriceTooLarge() {
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () -> sellableItemBuilder.parsePrice("~price 3000000 divine"));
        assertTrue(exception.getMessage().contains("Price is too large, got 3000000"));
    }
}
