package com.exileeconomics.service;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.repository.ItemDefinitionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemDefinitionsService {
    private final ItemDefinitionsRepository itemDefinitionsRepository;

    public ItemDefinitionsService(@Autowired ItemDefinitionsRepository itemDefinitionsRepository) {
        this.itemDefinitionsRepository = itemDefinitionsRepository;
    }

    public ItemDefinitionEntity getItemDefinitionEntityByItemDefinitionEnum(ItemDefinitionEnum itemDefinitionEnum) {
        return itemDefinitionsRepository.getItemDefinitionByName(itemDefinitionEnum.getName());
    }

    public Iterable<ItemDefinitionEntity> findAll() {
        return itemDefinitionsRepository.findAll();
    }

    public ItemDefinitionEntity save(ItemDefinitionEntity itemDefinitionEntity) {
        return itemDefinitionsRepository.save(itemDefinitionEntity);
    }

    public Collection<ItemDefinitionEntity> findAllItemDefinitionEntitiesByItemDefinitionEnums(Collection<ItemDefinitionEnum> itemSets) {
        return itemDefinitionsRepository.findAllByNameIn(
                itemSets.stream().map(ItemDefinitionEnum::getName).collect(Collectors.toSet())
        );
    }

    public ItemDefinitionEntity findFirsItemDefinitionByItemDefinitionEnum(ItemDefinitionEnum itemDefinitionEnum) {
        return itemDefinitionsRepository.getFirstByName(itemDefinitionEnum.getName());
    }

    public Iterable<ItemDefinitionEntity> saveAll(Iterable<ItemDefinitionEntity> entities) {
        return itemDefinitionsRepository.saveAll(entities);
    }
}
