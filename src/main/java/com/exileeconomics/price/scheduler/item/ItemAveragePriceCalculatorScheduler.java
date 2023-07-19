package com.exileeconomics.price.scheduler.item;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemAveragePriceEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.calculator.AverageItemPriceCalculator;
import com.exileeconomics.price.exception.AveragePriceCalculationException;
import com.exileeconomics.price.rules.ItemPriceRule;
import com.exileeconomics.price.rules.PriceRules;
import com.exileeconomics.price.rules.exceptions.RuleNotFoundException;
import com.exileeconomics.price.rules.quantity.QuantityLimitInterface;
import com.exileeconomics.service.CurrencyRatioService;
import com.exileeconomics.service.ItemAveragePriceEntityService;
import com.exileeconomics.service.ItemDefinitionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Component
public class ItemAveragePriceCalculatorScheduler {
    private final AverageItemPriceCalculator averageItemPriceCalculator;
    private final CurrencyRatioService currencyRatioService;
    private final ItemAveragePriceEntityService itemAveragePriceEntityService;
    private final ItemDefinitionsService itemDefinitionsService;
    private final PriceRules priceRules;

    public ItemAveragePriceCalculatorScheduler(
            @Autowired AverageItemPriceCalculator averageItemPriceCalculator,
            @Autowired ItemAveragePriceEntityService itemAveragePriceEntityService,
            @Autowired CurrencyRatioService currencyRatioService,
            @Autowired ItemDefinitionsService itemDefinitionsService,
            @Autowired PriceRules priceRules

    ) {
        this.averageItemPriceCalculator = averageItemPriceCalculator;
        this.itemAveragePriceEntityService = itemAveragePriceEntityService;
        this.currencyRatioService = currencyRatioService;
        this.itemDefinitionsService = itemDefinitionsService;
        this.priceRules = priceRules;
    }

    @Scheduled(cron = "0 2 23 * * *")
    public void scheduledItemPriceCalculationInsertBasedOnAveragePriceOfItemEntriesEvery12Hours() throws AveragePriceCalculationException, RuleNotFoundException {
        long start = System.currentTimeMillis();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());

        cal.add(Calendar.HOUR, -24);
        Timestamp nowMinus12Hours = new Timestamp(cal.getTime().getTime());

        Set<ItemDefinitionEnum> itemDefinitionEnums = new HashSet<>(Arrays.asList(ItemDefinitionEnum.values()));
        Collection<ItemDefinitionEntity> itemDefinitionEntities =
                itemDefinitionsService.findAllItemDefinitionEntitiesByItemDefinitionEnums(itemDefinitionEnums);

        ItemDefinitionEntity chaosOrbItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.CHAOS_ORB.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);
        ItemDefinitionEntity divineOrbItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.DIVINE_ORB.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);
        ItemDefinitionEntity sextantItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.AWAKENED_SEXTANT.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);

        Collection<CurrencyRatioEntity> itemCurrencyRatios = currencyRatioService.mostCurrentCurrencyRatio(List.of(
                        chaosOrbItemDefinitionEntity.getId(),
                        divineOrbItemDefinitionEntity.getId(),
                        sextantItemDefinitionEntity.getId()
                ), 3
        );

        System.out.println("Starting average price calculation...");
        // for each currency ratio in the database calculate the price
        // of each sold itemDefinition in the currency ratio
        for (CurrencyRatioEntity soldForItemEntity : itemCurrencyRatios) {
            List<ItemAveragePriceEntity> itemAveragePriceEntities = new ArrayList<>((int) (itemDefinitionEntities.size() + (itemDefinitionEntities.size() * 0.25)));

            for (ItemDefinitionEntity itemDefinitionEntity : itemDefinitionEntities) {
                ItemDefinitionEntity soldItemEntity = itemDefinitionEntities.stream().filter(n -> n.getName().equals(itemDefinitionEntity.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);

                ItemPriceRule rule = priceRules.getRuleForDefinitionByName(itemDefinitionEntity.getName());

                for (QuantityLimitInterface quantityLimits : rule.getQuantityLimits()) {
                    BigDecimal averagePrice;
                    try {
                        averagePrice = averageItemPriceCalculator.calculateAveragePriceFor(
                                nowMinus12Hours,
                                now,
                                soldItemEntity,
                                soldForItemEntity.getItemDefinitionEntity(),
                                rule.getLimit(),
                                rule.getOffset(),
                                quantityLimits.getQuantityLowerLimit(),
                                quantityLimits.getQuantityUpperLimit()
                        );
                    } catch (AveragePriceCalculationException e) {
                        continue; // intended
                    }

                    ItemAveragePriceEntity itemAveragePriceEntity = new ItemAveragePriceEntity();
                    itemAveragePriceEntity.setCurrencyRatio(
                            itemCurrencyRatios
                                    .stream()
                                    .filter(n -> n.getItemDefinitionEntity().getName().equals(soldForItemEntity.getItemDefinitionEntity().getName()))
                                    .findFirst()
                                    .orElseThrow(AveragePriceCalculationException::new)
                    );
                    itemAveragePriceEntity.setQuantityLowerLimit(quantityLimits.getQuantityLowerLimit());
                    itemAveragePriceEntity.setQuantityUpperLimit(quantityLimits.getQuantityUpperLimit());
                    itemAveragePriceEntity.setPrice(averagePrice);
                    itemAveragePriceEntity.setItem(soldItemEntity);
                    itemAveragePriceEntities.add(itemAveragePriceEntity);
                }
            }
            if (!itemAveragePriceEntities.isEmpty()) {
                itemAveragePriceEntityService.saveAll(itemAveragePriceEntities);
                itemAveragePriceEntities.clear();
            }
        }

        long finish = System.currentTimeMillis();
        System.out.printf("Finished scheduler for item price in %d milliseconds", (finish - start));
    }
}
