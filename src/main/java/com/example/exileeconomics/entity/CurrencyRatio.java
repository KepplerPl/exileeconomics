package com.example.exileeconomics.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;


/**
 * This table technically speaking should not exist since a 1-to-1 relationship
 * can be expressed using only the ItemDefinition table
 * <p>
 * But since not all items will have a currency ratio it's better to keep it separate
 */
@Entity
@Table(name = "currency_ratio")
public class CurrencyRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer chaos;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private ItemDefinition itemDefinition;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
    protected Timestamp createdAt;

    public ItemDefinition getItemDefinition() {
        return itemDefinition;
    }

    public void setItemDefinition(ItemDefinition itemDefinition) {
        this.itemDefinition = itemDefinition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChaos() {
        return chaos;
    }

    public void setChaos(Integer chaos) {
        this.chaos = chaos;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
