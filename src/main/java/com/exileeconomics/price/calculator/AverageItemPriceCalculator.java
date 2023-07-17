package com.exileeconomics.price.calculator;

import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.entity.ItemEntity;
import com.exileeconomics.price.exception.AveragePriceCalculationException;
import com.exileeconomics.service.ItemEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.OptionalDouble;

@Service
public class AverageItemPriceCalculator {
    private final ItemEntityService itemEntityService;

    public AverageItemPriceCalculator(
            @Autowired ItemEntityService itemEntityService
    ) {
        this.itemEntityService = itemEntityService;
    }

    public BigDecimal calculateAveragePriceFor(
            Timestamp lowerTimeLimit,
            Timestamp upperTimeLimit,
            ItemDefinitionEntity soldItemEntity,
            ItemDefinitionEntity soldForItemEntity,
            int limit,
            int offset,
            int quantityLowerLimit,
            int quantityUpperLimit
    ) throws AveragePriceCalculationException {
        Collection<ItemEntity> pricesInBetweenDates = itemEntityService.getPricesForItemsBetweenDatesWithLimitAndOffsetAndQuantityBetween(
                soldItemEntity.getId(),
                soldForItemEntity.getId(),
                quantityLowerLimit,
                quantityUpperLimit,
                lowerTimeLimit,
                upperTimeLimit,
                limit,
                offset
        );

        if (pricesInBetweenDates.isEmpty()) {
            throw new AveragePriceCalculationException("Average price is 0");
        }

        OptionalDouble averagePrice = pricesInBetweenDates.stream().mapToDouble(item -> item.getPrice().doubleValue()).average();
        if (averagePrice.isPresent()) {
            return BigDecimal.valueOf(averagePrice.getAsDouble());
        }
        throw new AveragePriceCalculationException("Unable to calculate price");
    }
}
