package com.example.exileeconomics.price.scheduler;

import com.example.exileeconomics.definitions.ItemDefinitionEnum;
import com.example.exileeconomics.entity.CurrencyRatioEntity;
import com.example.exileeconomics.entity.ItemDefinitionEntity;
import com.example.exileeconomics.entity.ItemEntity;
import com.example.exileeconomics.price.event.CurrencyRatioUpdateEvent;
import com.example.exileeconomics.price.exception.CurrencyRatioException;
import com.example.exileeconomics.repository.CurrencyRatioRepository;
import com.example.exileeconomics.repository.ItemDefinitionsRepository;
import com.example.exileeconomics.repository.ItemEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Component
public class CurrencyRatioEventScheduler {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CurrencyRatioRepository currencyRatioRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final ItemDefinitionsRepository itemDefinitionsRepository;

    /**
     * I noticed that 40 items is generally the sweet spot for a correct average price
     * <p>
     * the problem is that this can get HEAVILY skewed if there aren't enough items on the market
     */
    private final int IMPOSED_LIMIT = 40;

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

    public void publishCurrencyRatioRecalculatedEvent(Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap) {
        CurrencyRatioUpdateEvent customSpringEvent = new CurrencyRatioUpdateEvent(this, currencyRatioMap);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

//    @Scheduled(fixedRate = 999999)
    // At minute 0 past every 21st hour from 9 through 23.
    // Or in simpler terms runs at 9AM and again at 11PM, twice a day
//    @Transactional(rollbackFor = { RuntimeException.class, Error.class, CurrencyRatioException.class })
//    @Scheduled(cron = "0 0 9/21 * * *")
    public void scheduledCurrencyRatioUpdateBasedOnAveragePriceOfItems_Every12Hours() throws CurrencyRatioException {

        Map<ItemDefinitionEnum, CurrencyRatioEntity> currencyRatioMap = new HashMap<>();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now.getTime());

        cal.add(Calendar.HOUR, -12);
        Timestamp nowMinus12Hours = new Timestamp(cal.getTime().getTime());

        ItemDefinitionEntity chaosOrbEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.CHAOS_ORB.getName());

        Set<CurrencyRatioEntity> currencyRatioEntityList = new HashSet<>(5);

        // TODO This approach is a very naive way of calculating the prices, must think of something better
        // sextant average price calc
        ItemDefinitionEntity sextantEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.AWAKENED_SEXTANT.getName());


        // divine average price calc
        ItemDefinitionEntity divineOrbEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.DIVINE_ORB.getName());
        int divineAveragePrice = calculateAveragePriceForDivine(now, nowMinus12Hours, chaosOrbEntity, divineOrbEntity, sextantEntity);
//
//        CurrencyRatioEntity divineCurrencyRatio = new CurrencyRatioEntity();
//        divineCurrencyRatio.setItemDefinitionEntity(divineOrbEntity);
//        divineCurrencyRatio.setChaos(divineAveragePrice);
//        currencyRatioEntityList.add(divineCurrencyRatio);
//        currencyRatioMap.put(ItemDefinitionEnum.DIVINE_ORB, divineCurrencyRatio);
//
//
//        int sextantAveragePrice = calculateAveragePriceForSextant(now, nowMinus12Hours, chaosOrbEntity, sextantEntity);
//
//        CurrencyRatioEntity sextantCurrencyRatio = new CurrencyRatioEntity();
//        sextantCurrencyRatio.setItemDefinitionEntity(sextantEntity);
//        sextantCurrencyRatio.setChaos(sextantAveragePrice);
//        currencyRatioEntityList.add(sextantCurrencyRatio);
//        currencyRatioMap.put(ItemDefinitionEnum.AWAKENED_SEXTANT, sextantCurrencyRatio);
//
//        // add one for chaos as well, even though it's not calculated
//        ItemDefinitionEntity chaosEntity = itemDefinitionsRepository.getFirstByName(ItemDefinitionEnum.CHAOS_ORB.getName());
//        CurrencyRatioEntity chaosCurrencyRatio = new CurrencyRatioEntity();
//        chaosCurrencyRatio.setItemDefinitionEntity(chaosEntity);
//        chaosCurrencyRatio.setChaos(1);
//        currencyRatioEntityList.add(chaosCurrencyRatio);
//        currencyRatioMap.put(ItemDefinitionEnum.CHAOS_ORB, chaosCurrencyRatio);
//
//        // save after getting all so that an exception can trigger if anything goes wrong
//        currencyRatioRepository.saveAll(currencyRatioEntityList);

        publishCurrencyRatioRecalculatedEvent(currencyRatioMap);
    }

    private int calculateAveragePriceForSextant(Timestamp now, Timestamp nowMinus12Hours, ItemDefinitionEntity chaosOrbEntity, ItemDefinitionEntity sextantEntity) throws CurrencyRatioException {
        int offset = 10;

        Optional<Integer> averageSextantPrice = itemEntityRepository.getAveragePriceForItem(
                sextantEntity.getId(),
                chaosOrbEntity.getId(),
                9999,
                nowMinus12Hours,
                now,
                IMPOSED_LIMIT,
                offset
        );

        if(averageSextantPrice.isPresent()) {
            return averageSextantPrice.get();
        }

        throw new CurrencyRatioException("Unable to calculate price for awakened sextant");
    }

    private int calculateAveragePriceForDivine(Timestamp now,
                                               Timestamp nowMinus12Hours,
                                               ItemDefinitionEntity chaosOrbEntity,
                                               ItemDefinitionEntity divineOrbEntity,
                                               ItemDefinitionEntity sextantEntity
    ) throws CurrencyRatioException {

        Collection<CurrencyRatioEntity> currencyRatioEntities = currencyRatioRepository.getAllByItemDefinitionEntityInAndCreatedAtIsBetween(new HashSet<>(List.of(
                chaosOrbEntity,
                divineOrbEntity,
                sextantEntity
        )), nowMinus12Hours, now);

        CurrencyRatioEntity divineOrbRatio = null;

        for(CurrencyRatioEntity currencyRatio: currencyRatioEntities) {
            if(currencyRatio.getItemDefinitionEntity().getName().equals(divineOrbEntity.getName())) {
                divineOrbRatio = currencyRatio;
                break;
            }
        }

        if(null != divineOrbRatio) {
            Collection<ItemEntity> pricesInBetweenDates = itemEntityRepository.getPricesForItemsBetweenDates(
                    divineOrbEntity.getId(),
                    chaosOrbEntity.getId(),
                    9999,
                    nowMinus12Hours,
                    now
            );

            // exclude prices that have a deviation of more than 20% from the previous day price
            // this will hopefully act as a barrier for price fixing and such
            BigDecimal multiplicand = new BigDecimal("0.2").setScale(4, RoundingMode.UNNECESSARY);

            BigDecimal skewingUp = divineOrbRatio.getChaos().multiply(multiplicand).add(divineOrbRatio.getChaos());
            BigDecimal skewingDown = divineOrbRatio.getChaos().subtract(divineOrbRatio.getChaos().multiply(multiplicand));
            Iterator<ItemEntity> iter = pricesInBetweenDates.iterator();

            while(iter.hasNext()) {
                BigDecimal next = iter.next().getPrice();
                if(next.compareTo(skewingDown) < 0 || next.compareTo(skewingUp) > 0) {
                    iter.remove();
                }
            }

//            pricesInBetweenDates.removeIf(next -> next.getPrice().compareTo(skewingDown) < 0 || next.getPrice().compareTo(skewingUp) > 0);

            BigDecimal bigDecimal = new BigDecimal(String.valueOf(BigDecimal.ZERO));
            for(ItemEntity item :pricesInBetweenDates) {
                bigDecimal = bigDecimal.add(item.getPrice());
            }

            bigDecimal = bigDecimal.divide(new BigDecimal(pricesInBetweenDates.size()).setScale(4, RoundingMode.UNNECESSARY), RoundingMode.HALF_EVEN);
            boolean test = true;
        }

        int offset = 10;

        // and this is, in theory, the average divine price for the past 12 hours
        Optional<Integer> averageDivinePrice = itemEntityRepository.getAveragePriceForItem(
                divineOrbEntity.getId(),
                chaosOrbEntity.getId(),
                9999,
                nowMinus12Hours,
                now,
                IMPOSED_LIMIT,
                offset
        );

        if(averageDivinePrice.isPresent()) {
            return averageDivinePrice.get();
        }

        throw new CurrencyRatioException("Unable to calculate price for divine");
    }
}
