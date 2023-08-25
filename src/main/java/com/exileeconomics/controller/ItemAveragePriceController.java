package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemAveragePriceEntity;
import com.exileeconomics.price.dto.AverageItemPriceDTO;
import com.exileeconomics.price.dto.AveragePriceDTO;
import com.exileeconomics.price.rules.ItemPriceRule;
import com.exileeconomics.price.rules.PriceRules;
import com.exileeconomics.price.rules.exceptions.RuleNotFoundException;
import com.exileeconomics.price.rules.quantity.QuantityLimitInterface;
import com.exileeconomics.service.CachingService;
import com.exileeconomics.service.ItemAveragePriceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping(("price"))
@RestController
public class ItemAveragePriceController {
    private final ItemAveragePriceEntityService itemAveragePriceEntityService;
    private final PriceRules priceRules;
    private final CachingService cachingService;

    public ItemAveragePriceController(
            @Autowired ItemAveragePriceEntityService itemAveragePriceEntityService,
            @Autowired PriceRules priceRules,
            @Autowired CachingService cachingService
    ) {
        this.itemAveragePriceEntityService = itemAveragePriceEntityService;
        this.priceRules = priceRules;
        this.cachingService = cachingService;
    }

    @GetMapping("/average/{soldFor}")
    public ResponseEntity<?> getAveragePriceForAllItemsSoldFor(@PathVariable ItemDefinitionEnum soldFor) {
        // TODO implement this
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/average/{soldItem}/{soldFor}")
    public ResponseEntity<?> getAveragePriceForItem(@PathVariable ItemDefinitionEnum soldItem, @PathVariable ItemDefinitionEnum soldFor) throws RuleNotFoundException {
        String key = soldItem + "-" + soldFor;
        String result = cachingService.get(key);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        ItemPriceRule priceRulesForItem = priceRules.getRuleForDefinitionByName(soldItem.getName());

        SortedMap<String, List<AveragePriceDTO>> averagePriceForItems = new TreeMap<>();

        for(QuantityLimitInterface quantityLimit : priceRulesForItem.getQuantityLimits()) {
            List<AveragePriceDTO> averagePriceDTOS = new ArrayList<>();
            Collection<ItemAveragePriceEntity> items = itemAveragePriceEntityService.getPriceForItemsInBetweenQuantities(
                    soldItem, soldFor, quantityLimit.getQuantityLowerLimit(), quantityLimit.getQuantityUpperLimit()
            );

            if(items.isEmpty()) {
                continue;
            }

            for(ItemAveragePriceEntity item:items) {
                AveragePriceDTO averagePriceDTO = new AveragePriceDTO();
                averagePriceDTO.setTimestamp(item.getCreatedAt().getTime());
                averagePriceDTO.setPrice(item.getPrice().doubleValue());
                averagePriceDTOS.add(averagePriceDTO);
            }

            averagePriceForItems.put(
                    quantityLimit.getQuantityLowerLimit() + "-" + quantityLimit.getQuantityUpperLimit(),
                    averagePriceDTOS
            );
        }

        if(averagePriceForItems.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        AverageItemPriceDTO averageItemPriceDTO = new AverageItemPriceDTO();
        averageItemPriceDTO.setAveragePriceMap(averagePriceForItems);
        averageItemPriceDTO.setSoldFor(soldFor.getName());
        averageItemPriceDTO.setSoldItem(soldItem.getName());

        cachingService.set(key, averageItemPriceDTO, 1, TimeUnit.HOURS);

        return new ResponseEntity<>(averageItemPriceDTO, HttpStatus.OK);
    }
}
