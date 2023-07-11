package com.example.exileeconomics.mapper;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicStashTabsDao {
    private List<ItemDao> itemDaos = new ArrayList<>();
}
