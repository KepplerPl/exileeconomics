package com.example.exileeconomics.mapper;

import java.util.ArrayList;
import java.util.List;

public class PublicStashTabsDao {
    private List<ItemDao> itemDaos = new ArrayList<>();

    public List<ItemDao> getItemDaos() {
        return itemDaos;
    }

    public void setItemDaos(List<ItemDao> itemDaos) {
        this.itemDaos = itemDaos;
    }
}
