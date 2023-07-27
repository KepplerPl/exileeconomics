package com.exileeconomics.entity;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Table(name = "item_mods")
public class ItemEntityMod extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String itemMod;
    protected Timestamp createdAt;

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
    public String getMod() {
        return itemMod;
    }

    public void setMod(String mod) {
        this.itemMod = mod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemEntityMod that)) return false;
        return Objects.equals(getMod(), that.getMod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMod());
    }
}
