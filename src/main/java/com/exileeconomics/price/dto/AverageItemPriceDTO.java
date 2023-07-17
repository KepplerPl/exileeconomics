package com.exileeconomics.price.dto;

import java.util.SortedMap;
import java.util.List;

public class AverageItemPriceDTO {
    private String soldFor;
    private String soldItem;
    private SortedMap<String, List<AveragePriceDTO>> averagePriceMap;

    public String getSoldFor() {
        return soldFor;
    }

    public void setSoldFor(String soldFor) {
        this.soldFor = soldFor;
    }

    public String getSoldItem() {
        return soldItem;
    }

    public void setSoldItem(String soldItem) {
        this.soldItem = soldItem;
    }

    public SortedMap<String, List<AveragePriceDTO>> getAveragePriceMap() {
        return averagePriceMap;
    }

    public void setAveragePriceMap(SortedMap<String, List<AveragePriceDTO>> averagePriceMap) {
        this.averagePriceMap = averagePriceMap;
    }
}
