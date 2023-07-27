package com.exileeconomics.entity;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(cascade = { CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable (name = "item_item_mods",
                   joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_mod_id", referencedColumnName = "id")
    )
    private List<ItemEntityMod> mods = new ArrayList<>();
    protected Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        price.setScale(4, RoundingMode.UNNECESSARY);
    }

    public List<ItemEntityMod> getMods() {
        return mods;
    }

    public void setMods(List<ItemEntityMod> mods) {
        this.mods = mods;
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
