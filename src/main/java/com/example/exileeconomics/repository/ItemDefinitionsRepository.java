package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.ItemDefinition;
import org.springframework.data.repository.CrudRepository;

public interface ItemDefinitionsRepository extends CrudRepository<ItemDefinition, Long> {
    ItemDefinition getItemDefinitionByName(String name);

}
