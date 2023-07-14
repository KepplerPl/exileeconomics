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
import com.exileeconomics.repository.CurrencyRatioRepository;
import com.exileeconomics.repository.ItemAveragePriceEntityRepository;
import com.exileeconomics.repository.ItemDefinitionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemAveragePriceCalculatorScheduler {
    private final AverageItemPriceCalculator averageItemPriceCalculator;
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemAveragePriceEntityRepository itemAveragePriceEntityRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;
    private final PriceRules priceRules;

    public ItemAveragePriceCalculatorScheduler(
            @Autowired AverageItemPriceCalculator averageItemPriceCalculator,
            @Autowired ItemAveragePriceEntityRepository itemAveragePriceEntityRepository,
            @Autowired CurrencyRatioRepository currencyRatioRepository,
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository,
            @Autowired PriceRules priceRules

    ) {
        this.averageItemPriceCalculator = averageItemPriceCalculator;
        this.itemAveragePriceEntityRepository = itemAveragePriceEntityRepository;
        this.currencyRatioRepository = currencyRatioRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
        this.priceRules = priceRules;
    }

//    @Scheduled(cron = "0 0 9/21 * * *")
    @Scheduled(fixedRate = 99999999)
    public void scheduledItemPriceCalculationInsertBasedOnAveragePriceOfItemEntriesEvery12Hours() throws AveragePriceCalculationException, RuleNotFoundException {
        long start = System.currentTimeMillis();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());

        cal.add(Calendar.HOUR, -72);
        Timestamp nowMinus12Hours = new Timestamp(cal.getTime().getTime());

        Collection<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsRepository.findAllByNameIn(
                Arrays.stream(ItemDefinitionEnum.values()).map(ItemDefinitionEnum::getName).collect(Collectors.toSet())
        );

        ItemDefinitionEntity chaosOrbItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.CHAOS_ORB.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);
        ItemDefinitionEntity divineOrbItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.DIVINE_ORB.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);
        ItemDefinitionEntity sextantItemDefinitionEntity = itemDefinitionEntities.stream().filter(item -> item.getName().equals(ItemDefinitionEnum.AWAKENED_SEXTANT.getName())).findFirst().orElseThrow(AveragePriceCalculationException::new);

        Collection<CurrencyRatioEntity> itemCurrencyRatios = currencyRatioRepository.mostCurrentCurrencyRatio(List.of(
                        chaosOrbItemDefinitionEntity.getId(),
                        divineOrbItemDefinitionEntity.getId(),
                        sextantItemDefinitionEntity.getId()
                ),
                3
        );

        Set<ItemDefinitionEntity> currencyToCalculateFor = new HashSet<>() {{
            add(chaosOrbItemDefinitionEntity); // 568
            add(divineOrbItemDefinitionEntity); // 219
            add(sextantItemDefinitionEntity);
        }};

        System.out.println("Starting average price calculating...");
        for (ItemDefinitionEntity soldForItemEntity : currencyToCalculateFor) {

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
                                soldForItemEntity,
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
                                    .filter(n -> n.getItemDefinitionEntity().getName().equals(soldForItemEntity.getName()))
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
                itemAveragePriceEntityRepository.saveAll(itemAveragePriceEntities);
                itemAveragePriceEntities.clear();
            }
        }

        long finish = System.currentTimeMillis();
        System.out.printf("Finished scheduler for item price in %d milliseconds", (finish - start));
    }
}
