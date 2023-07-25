package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
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

        Iterator<ItemDefinitionEntity> iterator = itemDefinitionsService.findAll().iterator();
        List<ItemDefinitionResponseDTO> itemDefinitionResponseDTOS = new ArrayList<>();

        while(iterator.hasNext()) {
            ItemDefinitionEntity itemDefinitionEntity = iterator.next();
            ItemDefinitionResponseDTO itemDefinitionResponseDTO = new ItemDefinitionResponseDTO();
            itemDefinitionResponseDTO.setItemDefinitionEnum(ItemDefinitionEnum.fromString(itemDefinitionEntity.getName()));
            itemDefinitionResponseDTO.setIcon(itemDefinitionEntity.getIcon());
            itemDefinitionResponseDTO.setName(itemDefinitionEntity.getName());

            itemDefinitionResponseDTOS.add(itemDefinitionResponseDTO);
        }

        cachingService.set(ITEM_ENUM_DEFINITION_CACHE_KEY_NAME, itemDefinitionResponseDTOS, 1, TimeUnit.HOURS);

        return new ResponseEntity<>(itemDefinitionResponseDTOS, HttpStatus.OK);
    }
}
