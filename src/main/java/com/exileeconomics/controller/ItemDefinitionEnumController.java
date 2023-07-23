package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class ItemDefinitionEnumController {
    private final CachingService cachingService;

    public ItemDefinitionEnumController(@Autowired CachingService cachingService) {
        this.cachingService = cachingService;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAvailableItemDefinitionEnums() {
        String ITEM_ENUM_DEFINITION_CACHE_KEY_NAME = "ITEM_ENUM_DEFINITION";
        String result = cachingService.get(ITEM_ENUM_DEFINITION_CACHE_KEY_NAME);
        if(null != result) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        ItemDefinitionEnum[] itemNames = ItemDefinitionEnum.values();
        Map<ItemDefinitionEnum, String> itemEnumsAndNames = new HashMap<>();
        for (ItemDefinitionEnum itemName : itemNames) {
            itemEnumsAndNames.put(itemName, itemName.getName().substring(0,1).toUpperCase() + itemName.getName().substring(1).toLowerCase());
        }

        cachingService.set(ITEM_ENUM_DEFINITION_CACHE_KEY_NAME, itemEnumsAndNames, 1, TimeUnit.HOURS);

        return new ResponseEntity<>(itemEnumsAndNames, HttpStatus.OK);
    }
}
