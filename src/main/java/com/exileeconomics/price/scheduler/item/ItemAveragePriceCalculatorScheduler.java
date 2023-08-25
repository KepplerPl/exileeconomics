package com.exileeconomics.price.scheduler.item;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemAveragePriceEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.ParsableCurrency;
import com.exileeconomics.price.calculator.AverageItemPriceCalculator;
import com.exileeconomics.price.exception.AveragePriceCalculationException;
import com.exileeconomics.price.rules.ItemPriceRule;
import com.exileeconomics.price.rules.PriceRules;
import com.exileeconomics.price.rules.exceptions.RuleNotFoundException;
import com.exileeconomics.price.rules.quantity.QuantityLimitInterface;
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
    private final ItemAveragePriceEntityService itemAveragePriceEntityService;
    private final ItemDefinitionsService itemDefinitionsService;
    private final PriceRules priceRules;
    private final ParsableCurrency parsableCurrency;

    public ItemAveragePriceCalculatorScheduler(
            @Autowired AverageItemPriceCalculator averageItemPriceCalculator,
            @Autowired ItemAveragePriceEntityService itemAveragePriceEntityService,
            @Autowired ItemDefinitionsService itemDefinitionsService,
            @Autowired PriceRules priceRules,
            @Autowired ParsableCurrency parsableCurrency

    ) {
        this.averageItemPriceCalculator = averageItemPriceCalculator;
        this.itemAveragePriceEntityService = itemAveragePriceEntityService;
        this.itemDefinitionsService = itemDefinitionsService;
        this.priceRules = priceRules;
        this.parsableCurrency = parsableCurrency;
    }

//    @Scheduled(cron = "0 * * * * *")
    public void scheduledItemPriceCalculationInsertBasedOnAveragePriceOfItemEntriesEvery12Hours() throws AveragePriceCalculationException, RuleNotFoundException {
        long start = System.currentTimeMillis();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());

        cal.add(Calendar.HOUR, -24);
        Timestamp nowMinus12Hours = new Timestamp(cal.getTime().getTime());

        Set<ItemDefinitionEnum> itemDefinitionEnums = new HashSet<>(Arrays.asList(ItemDefinitionEnum.values()));
        Collection<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsService.findAllItemDefinitionEntitiesByItemDefinitionEnums(itemDefinitionEnums);
        Collection<CurrencyRatioEntity> itemCurrencyRatios = parsableCurrency.getMostRecentCurrencyRatioForParsableCurrency();

        System.out.println("Starting average price calculation...");
        // for each currency ratio in the database calculate the price
        // of each sold itemDefinition in that currency ratio
        //
        // for example
        // Essence of Hysteria -> Chaos Orb
        // Essence of Hysteria -> Divine Orb
        // Essence of Hysteria -> Awakened Sextant
        calculateAndSaveAveragePrice(now, nowMinus12Hours, itemDefinitionEntities, itemCurrencyRatios);

        long finish = System.currentTimeMillis();
        System.out.printf("Finished scheduler for item price in %d milliseconds", (finish - start));
    }

    private void calculateAndSaveAveragePrice(Timestamp now, Timestamp nowMinus12Hours, Collection<ItemDefinitionEntity> itemDefinitionEntities, Collection<CurrencyRatioEntity> itemCurrencyRatios) throws AveragePriceCalculationException, RuleNotFoundException {
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
    }
}
