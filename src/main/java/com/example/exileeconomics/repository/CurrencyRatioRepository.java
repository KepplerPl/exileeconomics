package com.example.exileeconomics.repository;

import com.example.exileeconomics.entity.CurrencyRatioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CurrencyRatioRepository extends CrudRepository<CurrencyRatioEntity, Long> {

    @Query(
            value = "SELECT * FROM currency_ratio WHERE item_definition_entity_id in (:list) ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true
    )
    Collection<CurrencyRatioEntity> mostCurrentCurrencyRatio(@Param("list") List<Long> list, @Param("limit") Integer limit);
}
