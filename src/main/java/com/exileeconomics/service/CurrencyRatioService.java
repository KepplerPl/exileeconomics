package com.exileeconomics.service;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.repository.CurrencyRatioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyRatioService {

    private final CurrencyRatioRepository currencyRatioRepository;

    public CurrencyRatioService(@Autowired CurrencyRatioRepository currencyRatioRepository) {
        this.currencyRatioRepository = currencyRatioRepository;
    }

    public Collection<CurrencyRatioEntity> mostCurrentCurrencyRatio(List<Long> list, Integer limit) {
        return currencyRatioRepository.mostCurrentCurrencyRatio(list, limit);
    }

    public Iterable<CurrencyRatioEntity> saveAll(Iterable<CurrencyRatioEntity> entities) {
        return  currencyRatioRepository.saveAll(entities);
    }

    public CurrencyRatioEntity save(CurrencyRatioEntity currencyRatio) {
        return currencyRatioRepository.save(currencyRatio);
    }

    public Iterable<CurrencyRatioEntity> findAll() {
        return currencyRatioRepository.findAll();
    }

    public Optional<CurrencyRatioEntity> getMostRecentCurrencyRatioByItemDefinition(ItemDefinitionEnum itemDefinitionEnum){
        return currencyRatioRepository.getMostRecentCurrencyRatioByItemDefinition(itemDefinitionEnum.getName());
    }
}
