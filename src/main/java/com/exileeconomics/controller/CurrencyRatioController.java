package com.exileeconomics.controller;

import com.exileeconomics.definitions.ItemDefinitionEnum;
import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.service.CurrencyRatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping(("ratio"))
@RestController
@CrossOrigin(origins = "*")
public class CurrencyRatioController {
    private final CurrencyRatioService currencyRatioService;

    public CurrencyRatioController(@Autowired CurrencyRatioService currencyRatioService) {
        this.currencyRatioService = currencyRatioService;
    }

    @GetMapping("/{item}")
    public ResponseEntity<?> getMostRecent(@PathVariable ItemDefinitionEnum item) {
        Optional<CurrencyRatioEntity> currencyRatio = currencyRatioService.getMostRecentCurrencyRatioByItemDefinition(item);
        if(currencyRatio.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(currencyRatio.get().getChaos(), HttpStatus.OK);
    }

}
