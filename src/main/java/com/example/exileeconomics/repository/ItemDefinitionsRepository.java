package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemDefinitionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ItemDefinitionsRepository extends CrudRepository<ItemDefinitionEntity, Long> {
    ItemDefinitionEntity getItemDefinitionByName(String name);

    Iterable<ItemDefinitionEntity> findAllByNameIn(Set<String> name);

}
