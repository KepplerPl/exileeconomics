package com.exileeconomics.repository;

import com.exileeconomics.entity.CurrencyRatioEntity;
import com.exileeconomics.entity.ItemDefinitionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CurrencyRatioRepository extends CrudRepository<CurrencyRatioEntity, Long> {
    @Query(
            value = "SELECT * FROM currency_ratio WHERE item_definition_entity_id in (:list) ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true
    )
    Collection<CurrencyRatioEntity> mostCurrentCurrencyRatio(@Param("list") List<Long> list, @Param("limit") Integer limit);
}
