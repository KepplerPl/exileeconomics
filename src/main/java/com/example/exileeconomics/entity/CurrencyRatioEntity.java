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
public class CurrencyRatioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer chaos;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    private ItemDefinitionEntity itemDefinitionEntity;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
    protected Timestamp createdAt;

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
