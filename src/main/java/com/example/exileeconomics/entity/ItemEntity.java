package com.example.exileeconomics.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    @Column(precision=10, scale=4)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ItemDefinitionEntity itemDefinitionEntity;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        price.setScale(4, RoundingMode.HALF_DOWN);
    }

    @PreUpdate
    public void pricePrecisionConvert() {
        price.setScale(4, RoundingMode.HALF_DOWN);
    }

    protected Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ItemDefinitionEntity getItemDefinition() {
        return itemDefinitionEntity;
    }

    public void setItemDefinition(ItemDefinitionEntity itemDefinitionEntity) {
        this.itemDefinitionEntity = itemDefinitionEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
