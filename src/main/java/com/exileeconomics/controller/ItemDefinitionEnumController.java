package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.service.CurrencyRatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class ItemDefinitionEnumController {
    @GetMapping("/items")
    public ResponseEntity<?> getAvailableItemDefinitionEnums() {
        return new ResponseEntity<>(ItemDefinitionEnum.values(), HttpStatus.OK);
    }
}
