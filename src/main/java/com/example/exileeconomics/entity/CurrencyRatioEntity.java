package com.example.exileeconomics.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "currency_ratio")
public class CurrencyRatioEntity extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer chaos;
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

    public ItemDefinitionEntity getItemDefinitionEntity() {
        return itemDefinitionEntity;
    }

    public void setItemDefinitionEntity(ItemDefinitionEntity itemDefinitionEntity) {
        this.itemDefinitionEntity = itemDefinitionEntity;
    }
}
