package com.example.exileeconomics.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ItemEntity extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int totalQuantity;
    private int soldQuantity;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CurrencyRatioEntity currencyRatio;
    @Column(precision=11, scale=4)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ItemDefinitionEntity item;
    protected Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        price.setScale(4, RoundingMode.UNNECESSARY);
    }

    @PreUpdate
    public void pricePrecisionConvert() {
        price.setScale(4, RoundingMode.UNNECESSARY);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public CurrencyRatioEntity getCurrencyRatio() {
        return currencyRatio;
    }

    public void setCurrencyRatio(CurrencyRatioEntity currencyRatio) {
        this.currencyRatio = currencyRatio;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ItemDefinitionEntity getItem() {
        return item;
    }

    public void setItem(ItemDefinitionEntity item) {
        this.item = item;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
