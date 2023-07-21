package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class ItemDefinitionEnumController {
    @GetMapping("/items")
    public ResponseEntity<?> getAvailableItemDefinitionEnums() {
        return new ResponseEntity<>(ItemDefinitionEnum.values(), HttpStatus.OK);
    }
}
