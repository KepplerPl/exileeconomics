package com.exileeconomics.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "item_average_price")
public class ItemAveragePriceEntity extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(precision=11, scale=4)
    private BigDecimal price;
    @ManyToOne
    private ItemDefinitionEntity item;
    private int quantityLowerLimit;
    private int quantityUpperLimit;
    @ManyToOne
    private CurrencyRatioEntity currencyRatio;
    protected Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
