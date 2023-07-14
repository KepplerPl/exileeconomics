package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemDefinitionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface ItemDefinitionsRepository extends CrudRepository<ItemDefinitionEntity, Long> {
    ItemDefinitionEntity getItemDefinitionByName(String name);
    Collection<ItemDefinitionEntity> findAllByNameIn(Set<String> names);
    ItemDefinitionEntity getFirstByName(String name);
}
