package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemAveragePriceEntity;
import com.exileeconomics.price.dto.AverageItemPriceDTO;
import com.exileeconomics.price.dto.AveragePriceDTO;
import com.exileeconomics.price.rules.ItemPriceRule;
import com.exileeconomics.price.rules.PriceRules;
import com.exileeconomics.price.rules.exceptions.RuleNotFoundException;
import com.exileeconomics.price.rules.quantity.QuantityLimitInterface;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemAveragePriceEntityService;
import com.exileeconomics.service.ItemDefinitionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping(("price"))
@RestController
@CrossOrigin(origins = "*")
public class ItemAveragePriceController {
    private final ItemAveragePriceEntityService itemAveragePriceEntityService;
    private final PriceRules priceRules;

    public ItemAveragePriceController(
            @Autowired ItemAveragePriceEntityService itemAveragePriceEntityService,
            @Autowired PriceRules priceRules
    ) {
        this.itemAveragePriceEntityService = itemAveragePriceEntityService;
        this.priceRules = priceRules;
    }

    @GetMapping("/average/{soldItem}/{soldFor}")
    public AverageItemPriceDTO getAveragePriceForItem(@PathVariable ItemDefinitionEnum soldItem, @PathVariable ItemDefinitionEnum soldFor) throws RuleNotFoundException {
        ItemPriceRule priceRulesForItem = priceRules.getRuleForDefinitionByName(soldItem.getName());

        SortedMap<String, List<AveragePriceDTO>> averagePriceForItems = new TreeMap<>();

        for(QuantityLimitInterface quantityLimit : priceRulesForItem.getQuantityLimits()) {
            List<AveragePriceDTO> averagePriceDTOS = new ArrayList<>();
            Collection<ItemAveragePriceEntity> items = itemAveragePriceEntityService.getPriceForItemsInBetweenQuantities(
                    soldItem, soldFor, quantityLimit.getQuantityLowerLimit(), quantityLimit.getQuantityUpperLimit()
            );

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

        AverageItemPriceDTO averageItemPriceDTO = new AverageItemPriceDTO();
        averageItemPriceDTO.setAveragePriceMap(averagePriceForItems);
        averageItemPriceDTO.setSoldFor(soldFor.getName());
        averageItemPriceDTO.setSoldItem(soldItem.getName());

        return averageItemPriceDTO;
    }
}
