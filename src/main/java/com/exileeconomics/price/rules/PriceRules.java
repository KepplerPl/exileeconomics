package com.exileeconomics.price.rules;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.price.rules.exceptions.RuleNotFoundException;
import com.exileeconomics.price.rules.quantity.DefaultQuantity025Limit;
import com.exileeconomics.price.rules.quantity.DefaultQuantity2550Limit;
import com.exileeconomics.price.rules.quantity.DefaultQuantity50AllLimit;
import com.exileeconomics.price.rules.quantity.QuantityLimit;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PriceRules {
    private final Set<ItemPriceRule> priceRules = new HashSet<>();

    public PriceRules() {
        generateRules();
    }

    public Set<? extends ItemPriceRuleInterface> getPriceRules() {
        return priceRules;
    }

    public ItemPriceRule getRuleForDefinitionByName(String itemDefinitionEnumName) throws RuleNotFoundException {
        return priceRules.stream().filter(item -> item.getItemDefinitionEnum().getName().equals(itemDefinitionEnumName)).findFirst().orElseThrow(RuleNotFoundException::new);
    }

    private void generateRules() {
        for(ItemDefinitionEnum itemDefinitionEnum: ItemDefinitionEnum.values()) {
            ItemPriceRule itemPriceRule;
            // for now just these 3 get special rules
            switch (itemDefinitionEnum) {
                case WILD_CRYSTALLISED_LIFEFORCE, VIVID_CRYSTALLISED_LIFEFORCE, PRIMAL_CRYSTALLISED_LIFEFORCE -> {
                    QuantityLimit quantityLimit1 = new QuantityLimit(0, 15000);
                    QuantityLimit quantityLimit2 = new QuantityLimit(15001, 30000);
                    QuantityLimit quantityLimit3 = new QuantityLimit(30001, 50000);
                    itemPriceRule = new ItemPriceRule(
                            itemDefinitionEnum,
                            40,
                            10,
                            Set.of(quantityLimit3, quantityLimit2, quantityLimit1)
                    );
                }
                case WINGED_ABYSS_SCARAB, WINGED_BESTIARY_SCARAB, WINGED_BLIGHT_SCARAB, WINGED_BREACH_SCARAB, WINGED_CARTOGRAPHY_SCARAB, WINGED_DIVINATION_SCARAB,
                        WINGED_ELDER_SCARAB, WINGED_EXPEDITION_SCARAB, WINGED_HARBINGER_SCARAB, WINGED_AMBUSH_SCARAB, WINGED_LEGION_SCARAB, WINGED_METAMORPH_SCARAB,
                        WINGED_RELIQUARY_SCARAB, WINGED_SHAPER_SCARAB, WINGED_SULPHITE_SCARAB, WINGED_TORMENT_SCARAB
                        -> {
                    QuantityLimit quantityLimit1 = new QuantityLimit(0, 20);
                    itemPriceRule = new ItemPriceRule(
                            itemDefinitionEnum,
                            20,
                            2,
                            Set.of(quantityLimit1)
                    );
                }
                default -> itemPriceRule = new ItemPriceRule(
                        itemDefinitionEnum,
                        40,
                        10,
                        Set.of(
                                new DefaultQuantity025Limit(),
                                new DefaultQuantity2550Limit(),
                                new DefaultQuantity50AllLimit()
                        )
                );
            }

            priceRules.add(itemPriceRule);
        }
    }
}
