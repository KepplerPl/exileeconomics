package com.exileeconomics.service;

import com.exileeconomics.entity.ItemEntityMod;
import com.exileeconomics.repository.ItemEntityModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemEntityModService {
    private final ItemEntityModRepository itemEntityModRepository;

    public ItemEntityModService(@Autowired ItemEntityModRepository itemEntityModRepository) {
        this.itemEntityModRepository = itemEntityModRepository;
    }

    public ItemEntityMod save(ItemEntityMod entity){
        return itemEntityModRepository.save(entity);
    }

    public Iterable<ItemEntityMod> findAll() {
        return itemEntityModRepository.findAll();
    }

    public Optional<ItemEntityMod> findFirstByMod(String mod){
        return itemEntityModRepository.findFirstByItemMod(mod);
    }
}
