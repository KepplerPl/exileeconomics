package com.example.exileeconomics.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "currency_ratio")
public class CurrencyRatioEntity extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal chaos;
    protected Timestamp createdAt;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private ItemDefinitionEntity itemDefinitionEntity;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getChaos() {
        return chaos;
    }

    public void setChaos(BigDecimal chaos) {
        this.chaos = chaos;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public ItemDefinitionEntity getItemDefinitionEntity() {
        return itemDefinitionEntity;
    }

    public void setItemDefinitionEntity(ItemDefinitionEntity itemDefinitionEntity) {
        this.itemDefinitionEntity = itemDefinitionEntity;
    }
}
