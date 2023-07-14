package com.exileeconomics.mapper;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicStashTabsDTO {
    private List<ItemDTO> itemDTOS = new ArrayList<>();
}
