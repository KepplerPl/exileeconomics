package com.exileeconomics.service;

import com.exileeconomics.entity.ItemEntity;
import com.exileeconomics.repository.ItemEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;

@Service
public class ItemEntityService {
    private final ItemEntityRepository itemEntityRepository;

    public ItemEntityService(@Autowired ItemEntityRepository itemEntityRepository) {
        this.itemEntityRepository = itemEntityRepository;
    }

    public Collection<ItemEntity> getPricesForItemsBetweenDatesWithLimitAndOffsetAndQuantityBetween(
            long soldItemId,
            long soldForItemId,
            int quantityLowerLimit,
            int quantityUpperLimit,
            Timestamp lowerTimeLimit,
            Timestamp upperTimeLimit,
            int limit,
            int offset
    ) {
        return itemEntityRepository.getPricesForItemsBetweenDatesWithLimitAndOffsetAndQuantityBetween(
                soldItemId,
                soldForItemId,
                quantityLowerLimit,
                quantityUpperLimit,
                lowerTimeLimit,
                upperTimeLimit,
                limit,
                offset
        );
    }

    public Iterable<ItemEntity> saveAll(Iterable<ItemEntity> entities) {
        return itemEntityRepository.saveAll(entities);
    }


    public ItemEntity save(ItemEntity entity) {
        return itemEntityRepository.save(entity);
    }

    public Collection<ItemEntity> getPricesForItemsBetweenDatesWithLimitAndOffset(
            long soldItemId,
            long soldForItemId,
            int totalQuantity,
            Timestamp lowerTimeLimit,
            Timestamp upperTimeLimit,
            int limit,
            int offset
    ){
        return itemEntityRepository.getPricesForItemsBetweenDatesWithLimitAndOffset(
                soldItemId,
                soldForItemId,
                totalQuantity,
                lowerTimeLimit,
                upperTimeLimit,
                limit,
                offset
        );
    }
}
