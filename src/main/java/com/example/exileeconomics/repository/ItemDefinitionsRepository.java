package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemDefinitionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemDefinitionsRepository extends CrudRepository<ItemDefinitionEntity, Long> {
    ItemDefinitionEntity getItemDefinitionByName(String name);
    Iterable<ItemDefinitionEntity> findAllByNameIn(Set<String> name);
    ItemDefinitionEntity getFirstByName(String name);
}
