package com.exileeconomics.controller;

import com.exileeconomics.cache.redis.TimeToLive;
import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.definitions.ItemDefinitionEnumCategoryMapper;
import com.exileeconomics.entity.ItemDefinitionEntity;
import com.exileeconomics.price.dto.ItemDefinitionResponseDTO;
import com.exileeconomics.service.CachingService;
import com.exileeconomics.service.ItemDefinitionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@RestController
public class ItemDefinitionEnumController {
    private final CachingService cachingService;
    private final ItemDefinitionsService itemDefinitionsService;

    public ItemDefinitionEnumController(
            @Autowired CachingService cachingService,
            @Autowired ItemDefinitionsService itemDefinitionsService
            ) {
        this.cachingService = cachingService;
        this.itemDefinitionsService = itemDefinitionsService;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAvailableItemDefinitionEnums() {
        String ITEM_ENUM_DEFINITION_CACHE_KEY_NAME = "ITEM_ENUM_DEFINITION";
        String result = cachingService.get(ITEM_ENUM_DEFINITION_CACHE_KEY_NAME);
        if(null != result) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        Iterable<ItemDefinitionEntity> itemDefinitionEntities = itemDefinitionsService.findAll();

       Map<ItemDefinitionEnumCategoryMapper, List<ItemDefinitionResponseDTO>> itemDefinitionEnumCategoryMapperListMap = StreamSupport
                .stream(itemDefinitionEntities.spliterator(), false)
                .map(itemDefinitionEntity -> {
                    ItemDefinitionResponseDTO itemDefinitionResponseDTO = new ItemDefinitionResponseDTO();
                    itemDefinitionResponseDTO.setMachineName(ItemDefinitionEnum.fromString(itemDefinitionEntity.getName()));
                    itemDefinitionResponseDTO.setIcon(itemDefinitionEntity.getIcon());
                    itemDefinitionResponseDTO.setReadableName(itemDefinitionEntity.getName());
                    return itemDefinitionResponseDTO;
                })
                .collect(groupingBy(
                        itemDefinitionEntity -> ItemDefinitionEnum.getCategoryFromName(itemDefinitionEntity.getMachineName().getName())
                ));

        cachingService.set(ITEM_ENUM_DEFINITION_CACHE_KEY_NAME, itemDefinitionEnumCategoryMapperListMap, new TimeToLive(1, TimeUnit.HOURS));

        return new ResponseEntity<>(itemDefinitionEnumCategoryMapperListMap, HttpStatus.OK);
    }
}
