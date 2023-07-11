package com.example.exileeconomics.price.scheduler;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.price.event.CurrencyRatioUpdateEvent;
import com.example.exileeconomics.price.exception.CurrencyRatioException;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;
import com.example.exileeconomics.repository.ItemEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class CurrencyRatioEventScheduler {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;

    public CurrencyRatioEventScheduler(
            @Autowired ApplicationEventPublisher applicationEventPublisher,
            @Autowired CurrencyRatioRepository currencyRatioRepository,
            @Autowired ItemEntityRepository itemEntityRepository,
            @Autowired ItemDefinitionsRepository itemDefinitionsRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.currencyRatioRepository = currencyRatioRepository;
        this.itemEntityRepository = itemEntityRepository;
        this.itemDefinitionsRepository = itemDefinitionsRepository;
    }

    public void publishCustomEvent(Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap) {
        CurrencyRatioUpdateEvent customSpringEvent = new CurrencyRatioUpdateEvent(this, currencyRatioMap);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    // At minute 0 past every 21st hour from 9 through 23.
    // Or in simpler terms runs at 9AM and again at 11PM, so twice a day
    @Scheduled(cron = "0 0 9/21 * * *")
    public void scheduledCurrencyRatioUpdateBasedOnAveragePriceOfItems_Every12Hours() throws CurrencyRatioException {
        Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap = new HashMap<>();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());

        cal.add(Calendar.HOUR, -12);
        Timestamp nowMinus12Hours = new Timestamp(cal.getTime().getTime());

        ItemDefinitionEntity chaosOrbEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.CHAOS_ORB.getName());

        Set<CurrencyRatioEntity> currencyRatioEntityList = new HashSet<>(5);
        // divine average price calc
        ItemDefinitionEntity divineOrbEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.DIVINE_ORB.getName());
        int divineAveragePrice = calculateAveragePriceForDivine(now, nowMinus12Hours, chaosOrbEntity, divineOrbEntity);

        CurrencyRatioEntity divineCurrencyRatio = new CurrencyRatioEntity();
        divineCurrencyRatio.setItemDefinitionEntity(divineOrbEntity);
        divineCurrencyRatio.setChaos(divineAveragePrice);
        currencyRatioEntityList.add(divineCurrencyRatio);
        currencyRatioMap.put(ItemDefinitionEnum.DIVINE_ORB, divineCurrencyRatio);

        // sextant average price calc
        ItemDefinitionEntity sextantEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.AWAKENED_SEXTANT.getName());
        int sextantAveragePrice = calculateAveragePriceForSextant(now, nowMinus12Hours, chaosOrbEntity, sextantEntity);

        CurrencyRatioEntity sextantCurrencyRatio = new CurrencyRatioEntity();
        sextantCurrencyRatio.setItemDefinitionEntity(sextantEntity);
        sextantCurrencyRatio.setChaos(sextantAveragePrice);
        currencyRatioEntityList.add(sextantCurrencyRatio);
        currencyRatioMap.put(ItemDefinitionEnum.AWAKENED_SEXTANT, sextantCurrencyRatio);


        // add one for chaos as well, even though it's not calculated
        ItemDefinitionEntity chaosEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.CHAOS_ORB.getName());
        CurrencyRatioEntity chaosCurrencyRatio = new CurrencyRatioEntity();
        chaosCurrencyRatio.setItemDefinitionEntity(chaosEntity);
        chaosCurrencyRatio.setChaos(1);
        currencyRatioEntityList.add(chaosCurrencyRatio);
        currencyRatioMap.put(ItemDefinitionEnum.CHAOS_ORB, chaosCurrencyRatio);

        // save after getting both so that an exception can trigger if that's the case
        currencyRatioRepository.saveAll(currencyRatioEntityList);

        publishCustomEvent(currencyRatioMap);
    }

    private int calculateAveragePriceForSextant(Timestamp now, Timestamp nowMinus12Hours, ItemDefinitionEntity chaosOrbEntity, ItemDefinitionEntity sextantEntity) throws CurrencyRatioException {
        int limit = 30;
        int offset = 0;

        Optional<Integer> averageSextantPrice = itemEntityRepository.getAveragePriceForItem(
                sextantEntity.getId(),
                chaosOrbEntity.getId(),
                nowMinus12Hours,
                now,
                limit,
                offset
        );

        if(averageSextantPrice.isPresent()) {
            return averageSextantPrice.get();
        }

        throw new CurrencyRatioException("Unable to calculate price for awakened sextant");
    }

    private int calculateAveragePriceForDivine(Timestamp now, Timestamp nowMinus12Hours, ItemDefinitionEntity chaosOrbEntity, ItemDefinitionEntity divineOrbEntity) throws CurrencyRatioException {
        //int totalDivinesOnMarket = itemEntityRepository.getTotalItemsInBetweenDates(
        //        divineOrbEntity.getId(),
        //        DIVINE_SOLD_QUANTITY_UPPER_LIMIT,
        //        nowMinus12Hours,
        //        now
        //);
        // this is where it gets tricky, in order to properly calculate the correct average price
        // ideally some percentage of items at the very start and very end need to be excluded
        // how many exactly it's hard to tell, for now I'm going with 10% at the start from the total items
        // and 30% of the total items at the end, since generally the very tail of the sold items are very
        // skewed in price towards the very top
        int limit = 30;
        int offset = 0;

        // and this is, in theory, the average divine price for the past 12 hours
        Optional<Integer> averageDivinePrice = itemEntityRepository.getAveragePriceForItem(
                divineOrbEntity.getId(),
                chaosOrbEntity.getId(),
                nowMinus12Hours,
                now,
                limit,
                offset
        );

        if(averageDivinePrice.isPresent()) {
            return averageDivinePrice.get();
        }

        throw new CurrencyRatioException("Unable to calculate price for divine");
    }

}
