package com.exileeconomics.service;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemAveragePriceEntity;
import com.exileeconomics.repository.ItemAveragePriceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class ItemAveragePriceEntityService {
    private final ItemAveragePriceEntityRepository itemAveragePriceEntityRepository;

    public ItemAveragePriceEntityService(@Autowired ItemAveragePriceEntityRepository itemAveragePriceEntityRepository) {
        this.itemAveragePriceEntityRepository = itemAveragePriceEntityRepository;
    }

    public Iterable<ItemAveragePriceEntity> saveAll(Iterable<ItemAveragePriceEntity> entities) {
        return itemAveragePriceEntityRepository.saveAll(entities);
    }

    public Collection<ItemAveragePriceEntity> getPriceForItemsInBetweenQuantities(ItemDefinitionEnum item, ItemDefinitionEnum currency, int quantityLowerLimit, int quantityUpperLimit) {
        return itemAveragePriceEntityRepository.getPriceForItemsInBetweenQuantities(item.getName(), currency.getName(), quantityLowerLimit, quantityUpperLimit);
    }
}
