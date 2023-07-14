package com.example.exileeconomics.price.calculator;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.entity.ItemEntity;
import com.example.exileeconomics.price.exception.AveragePriceCalculationException;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;
import com.example.exileeconomics.repository.ItemEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.OptionalDouble;
import java.util.Set;

@Service
public class AverageItemPriceCalculator {
    private final ItemEntityRepository itemEntityRepository;

    public AverageItemPriceCalculator(
            @Autowired ItemEntityRepository itemEntityRepository
    ) {
        this.itemEntityRepository = itemEntityRepository;
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
        Collection<ItemEntity> pricesInBetweenDates = itemEntityRepository.getPricesForItemsBetweenDatesWithLimitAndOffsetAndQuantityBetween(
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
