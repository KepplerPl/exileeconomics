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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequestMapping(("price"))
@RestController
public class ItemAveragePriceController {
    private final ItemAveragePriceEntityService itemAveragePriceEntityService;
    private final CurrencyRatioService currencyRatioService;
    private final ItemDefinitionsService itemDefinitionsService;
    private final PriceRules priceRules;

    public ItemAveragePriceController(
            @Autowired ItemAveragePriceEntityService itemAveragePriceEntityService,
            @Autowired CurrencyRatioService currencyRatioService,
            @Autowired PriceRules priceRules,
            @Autowired ItemDefinitionsService itemDefinitionsService
    ) {
        this.itemAveragePriceEntityService = itemAveragePriceEntityService;
        this.currencyRatioService = currencyRatioService;
        this.itemDefinitionsService = itemDefinitionsService;
        this.priceRules = priceRules;
    }

    @GetMapping("/average/{soldItem}/{soldFor}")
    public ResponseEntity<?> getAveragePriceForItem(@PathVariable ItemDefinitionEnum soldItem, @PathVariable ItemDefinitionEnum soldFor) throws RuleNotFoundException {
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

        return new ResponseEntity<>(averageItemPriceDTO, HttpStatus.OK);
    }
}
